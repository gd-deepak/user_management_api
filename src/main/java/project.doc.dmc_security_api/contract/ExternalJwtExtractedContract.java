package project.doc.dmc_security_api.contract;


import lombok.Data;

import java.util.List;

@Data
public class ExternalJwtExtractedContract {
    private String userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String name;
    private String email;
    private String exp;
    private String iat;

}