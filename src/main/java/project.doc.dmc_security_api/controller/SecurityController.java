package project.doc.dmc_security_api.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import project.doc.dmc_security_api.contract.AuthenticationRequest;
import project.doc.dmc_security_api.exceptions.InvalidRequestException;
import project.doc.dmc_security_api.exceptions.JwtValidationException;
import project.doc.dmc_security_api.exceptions.ResourceNotFoundException;
import project.doc.dmc_security_api.service.LoginService;

@Api(value = "dms_security_api", tags = {"security"})
@RestController
@RequestMapping({"/security"})
@CrossOrigin({"http://localhost:3000", "*"})
public class SecurityController {
    private LoginService loginService;

    @Autowired
    public SecurityController(LoginService loginService) {
        this.loginService = loginService;
    }

    @ApiOperation(value = "Validate login credentials and generate jwt token",
            notes = "request URL: /dms_security_api/security/login")
    @ApiResponses({@ApiResponse(code = 404, message = "User was not found")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping({"/login"})
    public ResponseEntity<ObjectNode> loginUser
            (@ApiParam(name = "login Authentication Request contract",type = "Object",
                    value = "login Authentication Request contract",example = "detail",required = true)
             @RequestBody AuthenticationRequest authRequest) throws InvalidRequestException, ResourceNotFoundException, JwtValidationException{
        return new ResponseEntity(this.loginService.authenticatedLogin(authRequest), HttpStatus.OK);
    }

    @ApiOperation(value = "Verify external jwt token and generate internal jwt",
            notes = "request URL: /dms_security_api/security/validate")
    @ApiResponses({@ApiResponse(code = 404, message = "User was not found")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping({"/validate"})
    public ResponseEntity<ObjectNode> validate(@RequestHeader(name = "Authorization") String jwtToken) throws JwtValidationException, ResourceNotFoundException, InvalidRequestException {
        return new ResponseEntity(this.loginService.validateExternalJwt(jwtToken), HttpStatus.OK);
    }

    @ApiOperation(value = "Verify jwt token and return response",
            notes = "request URL: /dms_security_api/security/userinfo")
    @ApiResponses({@ApiResponse(code = 404, message = "User was not found")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping({"/userinfo"})
    public ResponseEntity<ObjectNode> loginWithJwt(@RequestHeader(name = "Authorization") String jwtToken) throws JwtValidationException, ResourceNotFoundException, InvalidRequestException {
        String token = jwtToken.replace("Bearer ", "");
        return new ResponseEntity(this.loginService.authenticatedLoginWithJwt(token), HttpStatus.OK);
    }

    @ApiOperation(value = "logout", notes = "request URL: /dms_security_api/security/logout")
    @ApiResponses({@ApiResponse(code = 404, message = "User not found")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping({"/logout"})
    public ResponseEntity<?> logout(@RequestHeader(name = "Authorization") String jwtToken) {
        try {
            String token = jwtToken.replace("Bearer ", "");
            if (Boolean.TRUE.equals(this.loginService.logoutUser(token))) {
                return new ResponseEntity(HttpStatus.OK);
            } else {
                throw new InvalidRequestException("Invalid Token");
            }
        } catch (InvalidRequestException var3) {
            return new ResponseEntity("Invalid Token", HttpStatus.BAD_REQUEST);
        }
    }
}