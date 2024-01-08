package project.doc.dmc_security_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.doc.dmc_security_api.entity.RolePermission;

import java.util.UUID;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, UUID> {
    RolePermission findByRoleName(String roleName);
}
