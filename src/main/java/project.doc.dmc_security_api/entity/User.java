package project.doc.dmc_security_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.domain.Auditable;
import project.doc.dmc_security_api.constants.AuditTable;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "user", schema = "user_management")
public class User extends AuditTable<String>{
    @Id
    @GeneratedValue
    private UUID userId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "username")
    private String userName;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
}