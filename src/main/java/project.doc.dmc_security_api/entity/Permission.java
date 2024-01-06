package project.doc.dmc_security_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "permission", schema = "user_management")
public class Permission {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "permission_name")
    private String permissionName;

    @Column(name = "permission_description")
    private String permissionDescription;

}
