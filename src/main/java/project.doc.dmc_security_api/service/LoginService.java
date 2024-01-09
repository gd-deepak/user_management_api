package project.doc.dmc_security_api.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import project.doc.dmc_security_api.contract.AuthenticationRequest;
import project.doc.dmc_security_api.exceptions.InvalidRequestException;
import project.doc.dmc_security_api.exceptions.JwtValidationException;
import project.doc.dmc_security_api.exceptions.ResourceNotFoundException;

public interface LoginService {
    ObjectNode authenticatedLogin(AuthenticationRequest authRequest) throws JwtValidationException, InvalidRequestException, ResourceNotFoundException;

    ObjectNode returnLoginResponse(AuthenticationRequest authRequest) throws InvalidRequestException, ResourceNotFoundException, JwtValidationException;

    ObjectNode validateExternalJwt(String jwtToken) throws JwtValidationException, ResourceNotFoundException, InvalidRequestException;

    ObjectNode authenticatedLoginWithJwt(String jwtToken) throws JwtValidationException, ResourceNotFoundException, InvalidRequestException;

    Boolean logoutUser(String jwtToken);
}
