package project.doc.dmc_security_api.service.serviceImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import project.doc.dmc_security_api.constants.AuditTableAttribute;
import project.doc.dmc_security_api.contract.RoleContract;
import project.doc.dmc_security_api.contract.UserContract;
import project.doc.dmc_security_api.entity.Role;
import project.doc.dmc_security_api.entity.User;
import project.doc.dmc_security_api.entity.UserRole;
import project.doc.dmc_security_api.exceptions.InvalidRequestException;
import project.doc.dmc_security_api.exceptions.ResourceNotFoundException;
import project.doc.dmc_security_api.repo.RoleRepository;
import project.doc.dmc_security_api.repo.UserRepository;
import project.doc.dmc_security_api.repo.UserRoleRepository;
import project.doc.dmc_security_api.service.UserService;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final RoleRepository roleRepo;
    private final UserRoleRepository userRoleRepo;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           UserRoleRepository userRoleRepository,ModelMapper modelMapper,ObjectMapper objectMapper){
        this.repo=userRepository;
        this.roleRepo=roleRepository;
        this.userRoleRepo=userRoleRepository;
        this.modelMapper=modelMapper;
        this.objectMapper=objectMapper;
    }

    public JsonNode saveUser(UserContract contract) throws InvalidRequestException {
        User entity = this.modelMapper.map(contract, User.class);
        entity.setPassword(contract.getPassword());
        entity.setIsActive(true);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setModifiedDate(LocalDateTime.now());
        if (Boolean.TRUE.equals(this.repo.existsByEmail(entity.getEmail()))) {
            log.warn("Couldn't create user, already exists on email [{}]", entity.getEmail());
            throw new InvalidRequestException("Couldn't create user, email already exists.");
        } else {
            User savedEntity;
            try {
                savedEntity = this.repo.save(entity);
            } catch (Exception e) {
                log.error("Create user error: " + e.getMessage());
                throw new InvalidRequestException("Create user error: " + entity);
            }
            return this.objectMapper.valueToTree(modelMapper.map(savedEntity,UserContract.class));
    }
    }

    public List<JsonNode> findUsers() throws JsonProcessingException {

        List<UserContract> response = new ArrayList<>();
        Sort sort = Sort.by(Sort.Direction.DESC, AuditTableAttribute.MODIFIED_DATE.label);
        List<User> entities = this.repo.findAll(sort);

        for (User user:entities){
            RoleContract role=this.getUserRoles(user);
            UserContract userContract = modelMapper.map(user,UserContract.class);
            if (role != null){
                userContract.setRole(role);
            }
            response.add(userContract);
        }
        return objectMapper.readValue(this.objectMapper.writeValueAsString(response), new TypeReference<>() {
        });}
    public JsonNode findUserByUserId(String userId) throws ResourceNotFoundException {
        User user = repo.findByUserId(UUID.fromString(userId)).orElseThrow(
                () -> new ResourceNotFoundException("User not found on id: " + userId));
        RoleContract role = this.getUserRoles(user);
        UserContract userContract = this.modelMapper.map(user, UserContract.class);
        if (role != null){
            userContract.setRole(role);
        }
        return this.objectMapper.valueToTree(userContract);
    }

    public List<JsonNode> findUsersWithPredicate(String keywords, String sortOn, String sortOrder) throws JsonProcessingException {
        return this.objectMapper.readValue(this.objectMapper.writeValueAsString(new ArrayList<>()), new TypeReference<>() {
        });
    }

    public JsonNode updateUser(String userId, UserContract updateContract) throws ResourceNotFoundException, InvalidRequestException {
        log.info("Update user [{}].", updateContract.toString());
        User entity = repo.findByUserId(UUID.randomUUID()).orElseThrow(
                () -> new ResourceNotFoundException("User not found on id: " + userId));
        if (updateContract.getPassword() != null && updateContract.getPassword().length() < 20) {
            log.warn("Password should contain 20 or more than 20 characters");
            throw new InvalidRequestException("Password should contain 20 or more than 20 characters");
        } else {
            entity.setFirstName(StringUtils.isEmpty(updateContract.getFirstName()) ? entity.getFirstName() : updateContract.getFirstName());
            entity.setPassword(StringUtils.isEmpty(updateContract.getPassword()) ? entity.getPassword() : updateContract.getPassword());
            entity.setLastName(StringUtils.isEmpty(updateContract.getLastName()) ? entity.getLastName() : updateContract.getLastName());
            entity.setIsActive(updateContract.getIsActive());

            entity.setModifiedBy(null);
            entity.setModifiedDate(LocalDateTime.now());

            User updatedEntity;
            try {
                updatedEntity = repo.save(entity);
            } catch (Exception e) {
                log.error("update user error: " + e.getMessage());
                throw new InvalidRequestException("Update user error: " + entity);
            }

            UserContract userContract = this.modelMapper.map(updatedEntity, UserContract.class);
            return this.objectMapper.valueToTree(userContract);
        }
    }

    public JsonNode deleteUser(String userId) throws InvalidRequestException, ResourceNotFoundException {
        User entity = repo.findByUserId(UUID.fromString(userId)).orElseThrow(() -> new ResourceNotFoundException("User not found on id: " + userId));
        try {
            this.repo.delete(entity);
            log.info("User deleted for id [{}]", userId);
            return this.objectMapper.valueToTree(this.modelMapper.map(entity, UserContract.class));
        } catch (Exception var7) {
            log.error("Delete user on user id [{}] error [{}].", userId, var7.getMessage());
            throw new InvalidRequestException("Couldn't delete user on user id: " + userId);
        }
    }

    protected RoleContract getUserRoles(User user) {
        UserRole userRole= userRoleRepo.findByUserName(user.getUserName());
        RoleContract roleContract = null;
        if (userRole!=null){
            Role role= roleRepo.findByRoleName(userRole.getRoleName());
            modelMapper.map(role,RoleContract.class);
        }
        return roleContract;
    }

}
