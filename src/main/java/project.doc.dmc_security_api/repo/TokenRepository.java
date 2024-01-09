package project.doc.dmc_security_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import project.doc.dmc_security_api.entity.Token;

import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, String> {
}
