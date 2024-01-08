package project.doc.dmc_security_api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import project.doc.dmc_security_api.contract.PermissionContract;
import project.doc.dmc_security_api.exceptions.InvalidRequestException;
import project.doc.dmc_security_api.exceptions.ResourceNotFoundException;

import java.util.List;

public interface PermissionService {
    JsonNode savePermission(PermissionContract contract) throws InvalidRequestException;
    List<JsonNode> findPermissions() throws JsonProcessingException;
    JsonNode findByPermissionId(String permissionId) throws ResourceNotFoundException;
    List<JsonNode> findPermissionsWithPredicate(String keywords, String sortOn, String sortOrder) throws JsonProcessingException;
    JsonNode updatePermission(String permissionId, PermissionContract updateContract) throws ResourceNotFoundException, InvalidRequestException;
    JsonNode deletePermission(String permissionId) throws ResourceNotFoundException, InvalidRequestException;
}
