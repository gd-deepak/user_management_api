package project.doc.dmc_security_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Entity
@Table(name = "user_role", schema = "user_management")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {

    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "username",nullable = false
    )
    private String userName;
    @Column(name = "role_name",nullable = false)
    private String roleName;
}