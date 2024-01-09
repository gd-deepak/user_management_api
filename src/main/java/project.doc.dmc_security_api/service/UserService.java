package project.doc.dmc_security_api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import project.doc.dmc_security_api.contract.UserContract;
import project.doc.dmc_security_api.exceptions.InvalidRequestException;
import project.doc.dmc_security_api.exceptions.ResourceNotFoundException;

import java.util.List;

public interface UserService {
    JsonNode saveUser(UserContract contract) throws InvalidRequestException;
    List<JsonNode> findUsers() throws JsonProcessingException, ResourceNotFoundException;
    JsonNode findUserByUserId(String userId) throws ResourceNotFoundException;
    List<JsonNode> findUsersWithPredicate(String keywords, String sortOn, String sortOrder) throws JsonProcessingException;
    JsonNode updateUser(String userId, UserContract updateContract) throws ResourceNotFoundException, InvalidRequestException;
    JsonNode deleteUser(String userId) throws ResourceNotFoundException, InvalidRequestException;
}