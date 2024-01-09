package project.doc.dmc_security_api.service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.credential.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import project.doc.dmc_security_api.contract.AuthenticationRequest;
import project.doc.dmc_security_api.contract.RolePermissionDataContract;
import project.doc.dmc_security_api.dao.OktaValidation;
import project.doc.dmc_security_api.entity.Token;
import project.doc.dmc_security_api.entity.User;
import project.doc.dmc_security_api.entity.UserRole;
import project.doc.dmc_security_api.entity.Role;
import project.doc.dmc_security_api.entity.RolePermission;
import project.doc.dmc_security_api.entity.Permission;
import project.doc.dmc_security_api.exceptions.InvalidRequestException;
import project.doc.dmc_security_api.exceptions.JwtValidationException;
import project.doc.dmc_security_api.exceptions.ResourceNotFoundException;
import project.doc.dmc_security_api.repo.PermissionRepository;
import project.doc.dmc_security_api.repo.RolePermissionRepository;
import project.doc.dmc_security_api.repo.UserRepository;
import project.doc.dmc_security_api.repo.UserRoleRepository;
import project.doc.dmc_security_api.repo.RoleRepository;
import project.doc.dmc_security_api.repo.TokenRepository;
import project.doc.dmc_security_api.service.LoginService;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    @Value("${secrets-manager.jwt.id}")
    private String jwtSecretId;
    private ObjectMapper mapper;
    private PasswordService passwordService;
    private UserRepository userRepo;
    private PermissionRepository permissionRepo;
    private UserRoleRepository userRoleRepo;
    private RolePermissionRepository rolePermissionRepo;
    private RoleRepository roleRepo;
    private TokenRepository tokenRepo;
    private OktaValidation oktaValidation;

    @Autowired
    public LoginServiceImpl(ObjectMapper mapper,PasswordService passwordService, UserRepository userRepo,
                           PermissionRepository permissionRepo,TokenRepository tokenRepo,
                            UserRoleRepository userRoleRepo, RolePermissionRepository rolePermissionRepo,
                            RoleRepository roleRepo,
                            OktaValidation oktaValidation) {
        this.mapper = mapper;
        this.passwordService=passwordService;
        this.userRepo=userRepo;
        this.permissionRepo=permissionRepo;
        this.userRoleRepo=userRoleRepo;
        this.tokenRepo=tokenRepo;
        this.rolePermissionRepo=rolePermissionRepo;
        this.roleRepo=roleRepo;
        this.oktaValidation=oktaValidation;
    }

    @Override
    public ObjectNode authenticatedLogin(AuthenticationRequest authRequest) throws JwtValidationException, InvalidRequestException, ResourceNotFoundException {
        return returnLoginResponse(authRequest);
    }

    @Override
    public ObjectNode returnLoginResponse(AuthenticationRequest authRequest) throws InvalidRequestException, ResourceNotFoundException, JwtValidationException{

        User validatedUser = validateUser(authRequest);

        RolePermissionDataContract userProfileData = obtainUserRolesAndPermissions(validatedUser);

        populateUserProfileData(userProfileData, validatedUser,null);

        return mapper.valueToTree(userProfileData);
    }

    @Override
    public ObjectNode validateExternalJwt(String externalJwt) throws JwtValidationException, InvalidRequestException, ResourceNotFoundException {

        ObjectNode validatedJwtToken = oktaValidation.validate(externalJwt);

        User validatedUser = extractUserInformation(validatedJwtToken);

        if (validatedUser != null && Boolean.FALSE.equals(validatedUser.getIsActive())) {
            throw new InvalidRequestException("Access Denied: Your account is currently inactive.");
        }

        RolePermissionDataContract userProfileData = obtainUserRolesAndPermissions(validatedUser);

        populateUserProfileData(userProfileData, validatedUser,null);

        return mapper.valueToTree(userProfileData);
    }

    @Override
    public ObjectNode authenticatedLoginWithJwt(String jwtToken) throws JwtValidationException, ResourceNotFoundException, InvalidRequestException {

        Token token= tokenRepo.findById(jwtToken).orElseThrow(
                ()-> new ResourceNotFoundException("Invalid JWT token "+jwtToken));

        User validatedUser = userRepo.findByUserName(token.getUserName()).orElseThrow(
                ()-> new ResourceNotFoundException("User not found on Id " +token.getUserName()));

        if (validatedUser != null && Boolean.FALSE.equals(validatedUser.getIsActive())) {
            throw new InvalidRequestException("Access Denied: Your account is currently inactive.");
        }

        RolePermissionDataContract userProfileData = obtainUserRolesAndPermissions(validatedUser);

        populateUserProfileData(userProfileData,validatedUser,jwtToken);

        return mapper.valueToTree(userProfileData);
    }

    @Override
    public Boolean logoutUser(String jwtToken) {
        Optional<Token> entity = tokenRepo.findById(jwtToken);
        if (entity.isPresent()) {
            tokenRepo.delete(entity.get());
            return true;
        }
        return false;
    }

    protected User validateUser(AuthenticationRequest authRequest) throws InvalidRequestException, ResourceNotFoundException {
        if (StringUtils.isBlank(authRequest.getUsername())) {
            throw new InvalidRequestException("Username can't be null or empty.");
        }

        User entity = userRepo.findByUserName(authRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid username or password."));

        if (entity != null && Boolean.FALSE.equals(entity.getIsActive())) {
            throw new InvalidRequestException("Access Denied: Your account is currently inactive.");
        }

        if (!passwordService.passwordsMatch(authRequest.getPassword(), entity.getPassword())) {
            throw new InvalidRequestException("Invalid username or password.");
        }

        return entity;
    }

    protected RolePermissionDataContract obtainUserRolesAndPermissions(User user) throws ResourceNotFoundException {

        RolePermissionDataContract rolePermissionContract = new RolePermissionDataContract();

        UserRole userRole = userRoleRepo.findByUserName(user.getUserName());

        Role role = roleRepo.findByRoleName(userRole.getRoleName()).orElse(null);
        List<String> permissions = null;
        if (role != null) {
            List<RolePermission> rolePermissions = rolePermissionRepo.findByRoleName(role.getRoleName());
            for (RolePermission entity : rolePermissions){
                Permission permission = permissionRepo.findByPermissionName(entity.getPermissionName());
                permissions.add(permission.getPermissionName());
            }
        }
        rolePermissionContract.setRoleName(role.getRoleName());
        rolePermissionContract.setPermissions(permissions);
        return rolePermissionContract;
    }

    protected static String generateUserJwt(User user, RolePermissionDataContract rolePermissionDataContract, String jwtSecret) throws JwtValidationException {

        String jwtTokenStr = Jwts.builder()
                .claim("name", user.getUserName())
                .claim("role",rolePermissionDataContract.getRoleName())
                .claim("email", user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3000000L))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();

        return jwtTokenStr;
    }

    protected void storeJwt(User user, String jwtToken) {
        tokenRepo.save(new Token(jwtToken,user.getUserName()));
    }

    protected User extractUserInformation(ObjectNode extractedUserDetails) throws InvalidRequestException {
        String email = extractedUserDetails.get("email").asText();
        User entity = userRepo.findByEmail(email);

        if (entity != null && Boolean.FALSE.equals(entity.getIsActive())) {
            throw new InvalidRequestException("Access Denied: Your account is currently inactive.");
        }

        if (entity == null) {
            User registerUser = new User();
            registerUser.setFirstName(extractedUserDetails.get("given_name").asText());
            registerUser.setLastName(extractedUserDetails.get("family_name").asText());
            registerUser.setUserName(extractedUserDetails.get("preferred_username").asText());
            registerUser.setEmail(email);
            registerUser.setIsActive(true);
            registerUser.setCreatedDate(LocalDateTime.now());
            registerUser.setModifiedDate(LocalDateTime.now());
            entity = userRepo.save(registerUser);
            assignGeneralRole(entity);
        }

        return entity;
    }

    protected UserRole assignGeneralRole(User user){
        UserRole userRole=null;
        if (userRoleRepo.findByUserName(user.getUserName()).equals(null)) {
            Role generalRole =roleRepo.findByRoleName("General User").orElse(null);
            if (generalRole!=null){
                UserRole role = new UserRole();
                role.setId(null);
                role.setRoleName(generalRole.getRoleName());
                role.setUserName(user.getUserName());
                userRole= userRoleRepo.save(role);
            }
        } else{
            userRole = userRoleRepo.findByUserName(user.getUserName());
        }
        return userRole;
    }

    protected void populateUserProfileData(RolePermissionDataContract userProfileData, User validatedUser, String jwt) throws JwtValidationException {

        String fullName = validatedUser.getFirstName() + " " + validatedUser.getLastName();
        userProfileData.setName(fullName);
        userProfileData.setUserId(validatedUser.getUserId().toString());
        userProfileData.setUserName(validatedUser.getUserName());
        userProfileData.setFirstName(validatedUser.getFirstName());
        userProfileData.setLastName(validatedUser.getLastName());
        if (jwt == null) {
            log.info("secret key before JWT" + jwtSecretId);
            jwt = generateUserJwt(validatedUser, userProfileData, jwtSecretId);
            log.info("Secret kry after JWT" + jwtSecretId);
            storeJwt(validatedUser, jwt);
        }

        userProfileData.setJwt(jwt);
    }
}