package project.doc.dmc_security_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import project.doc.dmc_security_api.entity.UserRole;

import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
    UserRole findByUserName(String userName);
}
