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
@Table(name = "user", schema = "user_management")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    UUID userId;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "username")
    String userName;

    @Column(name = "email")
    String email;

    @Column(name = "password")
    String password;

    @ManyToOne
    @JoinColumn(name = "role_name")
    Role role;
}
