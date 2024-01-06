package project.doc.dmc_security_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.doc.dmc_security_api.entity.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

   boolean existsByEmail(String email);

   Optional<User> findByUserId(UUID userId);
}
