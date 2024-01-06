package project.doc.dmc_security_api.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "role", schema = "user_management")
public class Role {

    @Id
    @GeneratedValue
    @Column(name = "role_id")
    UUID roleId;

    @Column(name = "role_name")
    String roleName;

    @Column(name = "role_description")
    String roleDescription;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    List<UserRole> userRoles;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    List<RolePermission> rolePermissions;
}
