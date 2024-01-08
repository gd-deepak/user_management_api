package project.doc.dmc_security_api.service.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import project.doc.dmc_security_api.constants.AuditTableAttribute;
import project.doc.dmc_security_api.contract.RoleContract;
import project.doc.dmc_security_api.entity.Role;
import project.doc.dmc_security_api.entity.Permission;
import project.doc.dmc_security_api.entity.RolePermission;
import project.doc.dmc_security_api.exceptions.InvalidRequestException;
import project.doc.dmc_security_api.exceptions.ResourceNotFoundException;
import project.doc.dmc_security_api.repo.PermissionRepository;
import project.doc.dmc_security_api.repo.RolePermissionRepository;
import project.doc.dmc_security_api.repo.RoleRepository;
import project.doc.dmc_security_api.service.RoleService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repo;
    private final RolePermissionRepository rolePermissionRepo;
    private final PermissionRepository permissionRepo;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    @Autowired
    public RoleServiceImpl(RoleRepository repo, RolePermissionRepository rolePermissionRepo,PermissionRepository permissionRepo,
                           ModelMapper modelMapper, ObjectMapper objectMapper){
        this.repo=repo;
        this.rolePermissionRepo=rolePermissionRepo;
        this.permissionRepo =permissionRepo;
        this.modelMapper=modelMapper;
        this.objectMapper=objectMapper;
    }

    @Override
    public JsonNode saveRole(RoleContract contract) throws InvalidRequestException {
        Role entity = this.modelMapper.map(contract, Role.class);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setModifiedDate(LocalDateTime.now());
        if (Boolean.TRUE.equals(this.repo.existsByRoleName(entity.getRoleName()))) {
            log.warn("Couldn't create role, already exists on roleName [{}]", entity.getRoleName());
            throw new InvalidRequestException("Couldn't create role, role already exists.");
        } else {
            Role savedEntity;
            try {
                savedEntity = this.repo.save(entity);
            } catch (Exception e) {
                log.error("Create role error: " + e.getMessage());
                throw new InvalidRequestException("Create role error: " + entity);
            }
            return this.objectMapper.valueToTree(modelMapper.map(savedEntity, RoleContract.class));
        }
    }

    @Override
    public List<JsonNode> findRoles() throws JsonProcessingException {
        Sort sort = Sort.by(Sort.Direction.DESC, AuditTableAttribute.MODIFIED_DATE.label);
        List<Role> entities = this.repo.findAll(sort);
        return objectMapper.readValue(this.objectMapper.writeValueAsString(entities), new TypeReference<List<JsonNode>>() {});
    }

    @Override
    public JsonNode findByRoleId(String roleId) throws ResourceNotFoundException {
        Role role = repo.findByRoleId(UUID.fromString(roleId)).orElseThrow(() -> {
            return new ResourceNotFoundException("role not found on id: " + roleId);
        });
        Permission permissions = this.getRolePermissions(role);
        RoleContract roleContract = this.modelMapper.map(role, RoleContract.class);
        roleContract.setPermission(permissions);
        return this.objectMapper.valueToTree(roleContract);
    }

    @Override
    public List<JsonNode> findRolesWithPredicate(String keywords, String sortOn, String sortOrder) throws JsonProcessingException {
        return null;
    }

    @Override
    public JsonNode updateRole(String roleId, RoleContract updateContract) throws ResourceNotFoundException, InvalidRequestException {
        log.info("Update role [{}].", roleId.toString());
        Role entity = repo.findByRoleId(UUID.randomUUID()).orElseThrow(() -> {
            return new ResourceNotFoundException("Role not found on id: " + roleId);
        });
            entity.setRoleName(StringUtils.isEmpty(updateContract.getRoleName()) ? entity.getRoleName() : updateContract.getRoleName());
            entity.setRoleDescription(StringUtils.isEmpty(updateContract.getRoleDescription()) ? entity.getRoleDescription() : updateContract.getRoleDescription());

            entity.setModifiedBy(null);
            entity.setModifiedDate(LocalDateTime.now());

            Role updatedEntity;
            try {
                updatedEntity = repo.save(entity);
            } catch (Exception e) {
                log.error("update role error: " + e.getMessage());
                throw new InvalidRequestException("Update role error: " + entity);
            }

            RoleContract roleContract = this.modelMapper.map(updatedEntity, RoleContract.class);
            return this.objectMapper.valueToTree(roleContract);
    }
    @Override
    public JsonNode deleteRole(String roleId) throws ResourceNotFoundException, InvalidRequestException {
        Role entity = repo.findByRoleId(UUID.randomUUID()).orElseThrow(() -> {
            return new ResourceNotFoundException("Role not found on id: " + roleId);
        });
        try {
            this.repo.delete(entity);
            log.info("role deleted for id [{}]", roleId);
            return this.objectMapper.valueToTree(this.modelMapper.map(entity, RoleContract.class));
        } catch (Exception e) {
            log.error("Delete role on role id [{}] error [{}].", roleId, e.getMessage());
            throw new InvalidRequestException("Couldn't delete role on role id: " + roleId);
        }
    }

    protected Permission getRolePermissions(Role role) {
        RolePermission rolePermission = rolePermissionRepo.findByRoleName(role.getRoleName());
        Permission permission=null;
        if (rolePermission !=null) {
            permission = permissionRepo.findByPermissionName(rolePermission.getRoleName());
        }
        return permission;
    }
}
