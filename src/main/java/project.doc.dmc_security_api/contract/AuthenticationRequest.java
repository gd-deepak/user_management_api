package project.doc.dmc_security_api.contract;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}
