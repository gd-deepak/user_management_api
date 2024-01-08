package project.doc.dmc_security_api.controller;

import project.doc.dmc_security_api.contract.RoleContract;
import project.doc.dmc_security_api.exceptions.InvalidRequestException;
import project.doc.dmc_security_api.exceptions.ResourceNotFoundException;
import project.doc.dmc_security_api.service.RoleService;

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

import java.util.List;

@Slf4j
@Api(value = "dmc_security_api",tags = {"Role"})
@RestController
@RequestMapping({"/role"})
@CrossOrigin({"http://localhost:3000", "*"})
public class RoleController {
    private RoleService service;
    @Autowired
    private RoleController(RoleService service) {
        this.service = service;
    }

    @ApiOperation(value = "Create a role", notes = "request URL: /dmc_security_api/role")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Create Success", response = RoleContract.class),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal server error")})
    public ResponseEntity<JsonNode> createRole(
            @ApiParam(name = "Role contract", type = "Object", value = "Role contract", example = "detail", required = true)
            @RequestBody RoleContract contract) throws InvalidRequestException {
        log.info("Create a Role [{}]", contract);

        return new ResponseEntity<>(service.saveRole(contract), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get list of roles", notes = "request URL: /dmc_security_api/role")
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<List<JsonNode>> getRoles() throws JsonProcessingException {
        log.info("Get list of roles");

        return new ResponseEntity<>(service.findRoles(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get role by id", notes = "request URL: /dmc_security_api/role/{roleId}")
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/{roleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<JsonNode> getRoleByRoleId(
            @ApiParam(name = "roleId", type = "String", value = "roleId", example = "roleId0023", required = true)
            @PathVariable(name = "roleId", required = true) String roleId)
            throws ResourceNotFoundException {
        log.info("Find role by id [{}].", roleId);

        return new ResponseEntity<>(service.findByRoleId(roleId), HttpStatus.OK);
    }

    @ApiOperation(value = "Search/sort on roles", notes = "request URL: /role_management_api/role/search")
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<List<JsonNode>> getRolesWithPredicate(
            @ApiParam(name = "keywords", value = "search", type = "String", example = "a", required = false) @RequestParam(name = "keywords", required = false) String keywords,
            @ApiParam(name = "sortOn", type = "String", value = "sortBy", example = "contextId", required = false) @RequestParam(name = "sortOn", required = false) String sortOn,
            @ApiParam(name = "sortOrder", value = "asc/desc", type = "String", required = false) @RequestParam(name = "sortOrder", required = false) String sortOrder)
            throws JsonProcessingException {
        log.info("Search on role with given keyword(s) [{}] or sort on role field [{}] in [{}].", keywords, sortOn, sortOrder);

        return new ResponseEntity<>(service.findRolesWithPredicate(keywords, sortOn, sortOrder), HttpStatus.OK);
    }

    @ApiOperation(value = "Update the role", notes = "request URL: /dmc_security_api/role")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @PutMapping(path = "/{roleId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 202, message = "Request Accepted", response = RoleContract.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal server error")})
    public ResponseEntity<JsonNode> updateRole(
            @ApiParam(name = "roleId", type = "String", value = "roleId", example = "roleId0023", required = true) @PathVariable(name = "roleId", required = false) String roleId,
            @ApiParam(name = "role contract", type = "Object", value = "Role contract", example = "detail", required = true) @RequestBody RoleContract contract)
            throws ResourceNotFoundException, InvalidRequestException {
        log.info("Update role by roleId [{}]", roleId);

        return new ResponseEntity<>(service.updateRole(roleId, contract), HttpStatus.ACCEPTED);
    }

    @ApiOperation(value = "Delete a role by role id", notes = "request URL: /dmc_security_api/role/{roleId}")
    @ResponseStatus(code = HttpStatus.OK)
    @DeleteMapping(path = "/{roleId}")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")})
    public ResponseEntity<JsonNode> deleteRole(
            @ApiParam(name = "roleId", type = "String", value = "roleId", example = "43f0fa8b-7164-4159-a02d-3d4cd7ba8d54", required = true) @PathVariable String roleId)
            throws ResourceNotFoundException, InvalidRequestException {
        log.info("Delete a role by role id [{}]", roleId);

        return new ResponseEntity<>(service.deleteRole(roleId), HttpStatus.OK);
    }
}