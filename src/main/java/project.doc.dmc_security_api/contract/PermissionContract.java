package project.doc.dmc_security_api.contract;

import lombok.Data;

import java.util.UUID;

@Data
public class PermissionContract {
    private String permissionName;
    private String permissionDescription;
}
