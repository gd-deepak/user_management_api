package project.doc.dmc_security_api.contract;

import lombok.Data;

import java.util.List;

@Data
public class RolePermissionDataContract extends ExternalJwtExtractedContract {
    private String roleName;
    private List<String> permissions;
    private String jwt;
}
