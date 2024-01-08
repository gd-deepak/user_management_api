package project.doc.dmc_security_api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import project.doc.dmc_security_api.contract.RoleContract;
import project.doc.dmc_security_api.exceptions.InvalidRequestException;
import project.doc.dmc_security_api.exceptions.ResourceNotFoundException;

import java.util.List;

public interface RoleService {
    JsonNode saveRole(RoleContract contract) throws InvalidRequestException;
    List<JsonNode> findRoles() throws JsonProcessingException;
    JsonNode findByRoleId(String roleId) throws ResourceNotFoundException;
    List<JsonNode> findRolesWithPredicate(String keywords, String sortOn, String sortOrder) throws JsonProcessingException;
    JsonNode updateRole(String roleId, RoleContract roleContract) throws ResourceNotFoundException, InvalidRequestException;
    JsonNode deleteRole(String roleId) throws ResourceNotFoundException, InvalidRequestException;
}
