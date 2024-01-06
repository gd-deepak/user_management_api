package project.doc.dmc_security_api.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import java.util.UUID;

@Entity
@Table(name = "permission", schema = "user_management")
public class Permission{
    @Id
    @GeneratedValue
    @Column(name = "pId")
    UUID pId;

    @Column(name = "permission_name")
    String permissionName;

    @Column(name = "permission_description")
    String permissionDescription;

    @ManyToOne
    @JoinColumn(name = "role_name")
    Role role;

}
