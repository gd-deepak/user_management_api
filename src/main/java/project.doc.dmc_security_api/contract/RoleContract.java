package project.doc.dmc_security_api.contract;

import lombok.Data;
import project.doc.dmc_security_api.entity.Permission;

import java.util.UUID;

@Data
public class RoleContract {
    private UUID roleId;
    private String roleName;
    private String roleDescription;
    private Permission permission;

}
