package project.doc.dmc_security_api.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import project.doc.dmc_security_api.constants.AuditTable;


import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "permission", schema = "user_management")
@Data
public class Permission extends AuditTable<String> {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "permission_name")
    private String permissionName;

    @Column(name = "permission_description")
    private String permissionDescription;

}
