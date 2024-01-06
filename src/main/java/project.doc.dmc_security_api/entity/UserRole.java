package project.doc.dmc_security_api.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "user_role", schema = "user_management")
public class UserRole {

    @Id
    @GeneratedValue
    @Column(name = "id")
    UUID id;

    @ManyToOne
    @JoinColumn(name = "username")
    User user;

    @ManyToOne
    @JoinColumn(name = "role_name")
    Role role;
}
