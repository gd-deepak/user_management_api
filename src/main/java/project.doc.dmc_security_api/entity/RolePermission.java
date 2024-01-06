package project.doc.dmc_security_api.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "role_permission", schema = "user_management")
public class RolePermission {

    @Id
    @GeneratedValue
    @Column(name = "id")
    UUID id;

    @Column(name = "role_name", nullable = false
    )
    private String roleName;
    @Column(name = "permission_name", nullable = false)
    private String permissionName;
}
