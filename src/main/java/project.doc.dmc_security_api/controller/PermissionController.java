package project.doc.dmc_security_api.controller;

import project.doc.dmc_security_api.contract.PermissionContract;
import project.doc.dmc_security_api.exceptions.InvalidRequestException;
import project.doc.dmc_security_api.exceptions.ResourceNotFoundException;
import project.doc.dmc_security_api.service.PermissionService;

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
@Api(value = "dmc_security_api",tags = {"Permission"})
@RestController
@RequestMapping({"/permission"})
@CrossOrigin({"http://localhost:3000", "*"})
public class PermissionController {
    private PermissionService service;
    @Autowired
    private PermissionController(PermissionService service) {
        this.service = service;
    }

    @ApiOperation(value = "Create a permission", notes = "request URL: /dmc_security_api/permission")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Create Success", response = PermissionContract.class),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal server error")})
    public ResponseEntity<JsonNode> createPermission(
            @ApiParam(name = "Permission contract", type = "Object", value = "Permission contract", example = "detail", required = true)
            @RequestBody PermissionContract contract) throws InvalidRequestException {
        log.info("Create a Permission [{}]", contract);

        return new ResponseEntity<>(service.savePermission(contract), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get list of permissions", notes = "request URL: /dmc_security_api/permission")
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<List<JsonNode>> getPermissions() throws JsonProcessingException {
        log.info("Get list of permissions");

        return new ResponseEntity<>(service.findPermissions(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get permission by id", notes = "request URL: /dmc_security_api/permission/{permissionId}")
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/{permissionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<JsonNode> getPermissionByPermissionId(
            @ApiParam(name = "permissionId", type = "String", value = "permissionId", example = "permissionId0023", required = true)
            @PathVariable(name = "permissionId", required = true) String permissionId)
            throws ResourceNotFoundException {
        log.info("Find permission by id [{}].", permissionId);

        return new ResponseEntity<>(service.findByPermissionId(permissionId), HttpStatus.OK);
    }

    @ApiOperation(value = "Search/sort on permissions", notes = "request URL: /permission_management_api/permission/search")
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<List<JsonNode>> getPermissionsWithPredicate(
            @ApiParam(name = "keywords", value = "search", type = "String", example = "a", required = false) @RequestParam(name = "keywords", required = false) String keywords,
            @ApiParam(name = "sortOn", type = "String", value = "sortBy", example = "contextId", required = false) @RequestParam(name = "sortOn", required = false) String sortOn,
            @ApiParam(name = "sortOrder", value = "asc/desc", type = "String", required = false) @RequestParam(name = "sortOrder", required = false) String sortOrder)
            throws JsonProcessingException {
        log.info("Search on permission with given keyword(s) [{}] or sort on permission field [{}] in [{}].", keywords, sortOn, sortOrder);

        return new ResponseEntity<>(service.findPermissionsWithPredicate(keywords, sortOn, sortOrder), HttpStatus.OK);
    }

    @ApiOperation(value = "Update the permission", notes = "request URL: /dmc_security_api/permission")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @PutMapping(path = "/{permissionId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 202, message = "Request Accepted", response = PermissionContract.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal server error")})
    public ResponseEntity<JsonNode> updatePermission(
            @ApiParam(name = "permissionId", type = "String", value = "permissionId", example = "permissionId0023", required = true) @PathVariable(name = "permissionId", required = false) String permissionId,
            @ApiParam(name = "permission contract", type = "Object", value = "Permission contract", example = "detail", required = true) @RequestBody PermissionContract contract)
            throws ResourceNotFoundException, InvalidRequestException {
        log.info("Update permission by permissionId [{}]", permissionId);

        return new ResponseEntity<>(service.updatePermission(permissionId, contract), HttpStatus.ACCEPTED);
    }

    @ApiOperation(value = "Delete a permission by permission id", notes = "request URL: /dmc_security_api/permission/{permissionId}")
    @ResponseStatus(code = HttpStatus.OK)
    @DeleteMapping(path = "/{permissionId}")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")})
    public ResponseEntity<JsonNode> deletePermission(
            @ApiParam(name = "permissionId", type = "String", value = "permissionId", example = "43f0fa8b-7164-4159-a02d-3d4cd7ba8d54", required = true) @PathVariable String permissionId)
            throws ResourceNotFoundException, InvalidRequestException {
        log.info("Delete a permission by permission id [{}]", permissionId);

        return new ResponseEntity<>(service.deletePermission(permissionId), HttpStatus.OK);
    }
}