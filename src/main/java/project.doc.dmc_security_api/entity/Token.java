package project.doc.dmc_security_api.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(
        name = "token",
        schema = "user_management"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id
    @Column(
            name = "key",
            nullable = false
    )
    private String jwtToken;
    @Column(
            name = "username",
            nullable = false
    )
    private String userName;
}