package project.doc.dmc_security_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.doc.dmc_security_api.entity.Role;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional <Role> findByRoleName(String roleName);
    Boolean existsByRoleName(String roleName);
    Optional<Role> findByRoleId(UUID uuid);
}
