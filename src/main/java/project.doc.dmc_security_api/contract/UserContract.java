package project.doc.dmc_security_api.contract;

import lombok.Data;
import project.doc.dmc_security_api.entity.Role;

import java.util.UUID;

@Data
public class UserContract {
    UUID userId;
    String firstName;
    String lastName;
    String userName;
    String email;
    String password;
    Boolean isActive;
    Role role;
}
