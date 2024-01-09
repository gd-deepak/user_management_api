package project.doc.dmc_security_api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import project.doc.dmc_security_api.contract.UserContract;
import project.doc.dmc_security_api.exceptions.InvalidRequestException;
import project.doc.dmc_security_api.exceptions.ResourceNotFoundException;
import project.doc.dmc_security_api.service.UserService;

import java.util.List;

@Slf4j
@Api(value = "dmc_security_api",tags = {"User"})
@RestController
@RequestMapping({"/user"})
@CrossOrigin({"http://localhost:3000", "*"})
public class UserController {

    private UserService service;
    @Autowired
    public UserController(UserService service){
        this.service = service;
    }

    @ApiOperation(value = "Create a user", notes = "request URL: /dmc_security_api/user")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Create Success", response = UserContract.class),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal server error")})
    public ResponseEntity<JsonNode> createUser(
            @ApiParam(name = "User contract", type = "Object", value = "User contract", example = "detail", required = true)
            @RequestBody UserContract contract) throws InvalidRequestException {
        log.info("Create a User [{}]", contract);

        return new ResponseEntity<>(service.saveUser(contract), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get list of users", notes = "request URL: /dmc_security_api/user")
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<List<JsonNode>> getUsers() throws JsonProcessingException, ResourceNotFoundException {
        log.info("Get list of users");

        return new ResponseEntity<>(service.findUsers(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get user by id", notes = "request URL: /dmc_security_api/user/{userId}")
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<JsonNode> getUserByUserId(
            @ApiParam(name = "userId", type = "String", value = "userId", example = "userId0023", required = true)
            @PathVariable(name = "userId", required = true) String userId)
            throws ResourceNotFoundException {
        log.info("Find user by id [{}].", userId);

        return new ResponseEntity<>(service.findUserByUserId(userId), HttpStatus.OK);
    }

    @ApiOperation(value = "Search/sort on users", notes = "request URL: /user_management_api/user/search")
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<List<JsonNode>> getUsersWithPredicate(
            @ApiParam(name = "keywords", value = "search", type = "String", example = "a", required = false) @RequestParam(name = "keywords", required = false) String keywords,
            @ApiParam(name = "sortOn", type = "String", value = "sortBy", example = "contextId", required = false) @RequestParam(name = "sortOn", required = false) String sortOn,
            @ApiParam(name = "sortOrder", value = "asc/desc", type = "String", required = false) @RequestParam(name = "sortOrder", required = false) String sortOrder)
            throws JsonProcessingException {
        log.info("Search on user with given keyword(s) [{}] or sort on user field [{}] in [{}].", keywords, sortOn, sortOrder);

        return new ResponseEntity<>(service.findUsersWithPredicate(keywords, sortOn, sortOrder), HttpStatus.OK);
    }

    @ApiOperation(value = "Update the user", notes = "request URL: /dmc_security_api/user")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @PutMapping(path = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 202, message = "Request Accepted", response = UserContract.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal server error")})
    public ResponseEntity<JsonNode> updateUser(
            @ApiParam(name = "userId", type = "String", value = "userId", example = "userId0023", required = true) @PathVariable(name = "userId", required = false) String userId,
            @ApiParam(name = "user contract", type = "Object", value = "User contract", example = "detail", required = true) @RequestBody UserContract contract)
            throws ResourceNotFoundException, InvalidRequestException {
        log.info("Update user by userId [{}]", userId);

        return new ResponseEntity<>(service.updateUser(userId, contract), HttpStatus.ACCEPTED);
    }

    @ApiOperation(value = "Delete a user by user id", notes = "request URL: /dmc_security_api/user/{userId}")
    @ResponseStatus(code = HttpStatus.OK)
    @DeleteMapping(path = "/{userId}")
    @ApiResponses(value = { @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")})
    public ResponseEntity<JsonNode> deleteUser(
            @ApiParam(name = "userId", type = "String", value = "userId", example = "43f0fa8b-7164-4159-a02d-3d4cd7ba8d54", required = true) @PathVariable String userId)
            throws ResourceNotFoundException, InvalidRequestException {
        log.info("Delete a user by user id [{}]", userId);

        return new ResponseEntity<>(service.deleteUser(userId), HttpStatus.OK);
    }
}