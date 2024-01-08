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
import project.doc.dmc_security_api.contract.PermissionContract;
import project.doc.dmc_security_api.entity.Permission;
import project.doc.dmc_security_api.exceptions.InvalidRequestException;
import project.doc.dmc_security_api.exceptions.ResourceNotFoundException;
import project.doc.dmc_security_api.repo.PermissionRepository;
import project.doc.dmc_security_api.service.PermissionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {
    
    PermissionRepository repo;
    ModelMapper modelMapper;
    ObjectMapper objectMapper;
    @Autowired
    public PermissionServiceImpl(PermissionRepository repo,ModelMapper modelMapper,ObjectMapper objectMapper){
        this.repo=repo;
        this.modelMapper=modelMapper;
        this.objectMapper=objectMapper;
    }
    @Override
    public JsonNode savePermission(PermissionContract contract) throws InvalidRequestException {
        Permission entity = this.modelMapper.map(contract, Permission.class);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setModifiedDate(LocalDateTime.now());
        if (Boolean.TRUE.equals(this.repo.existsByPermissionName(entity.getPermissionName()))) {
            log.warn("Couldn't create Permission, already exists on PermissionName [{}]", entity.getPermissionName());
            throw new InvalidRequestException("Couldn't create Permission, Permission already exists.");
        } else {
            Permission savedEntity;
            try {
                savedEntity = this.repo.save(entity);
            } catch (Exception e) {
                log.error("Create Permission error: " + e.getMessage());
                throw new InvalidRequestException("Create Permission error: " + entity);
            }
            return this.objectMapper.valueToTree(modelMapper.map(savedEntity, PermissionContract.class));
        }
    }

    @Override
    public List<JsonNode> findPermissions() throws JsonProcessingException {
        Sort sort = Sort.by(Sort.Direction.DESC, AuditTableAttribute.MODIFIED_DATE.label);
        List<Permission> entities = this.repo.findAll(sort);
        return objectMapper.readValue(this.objectMapper.writeValueAsString(entities), new TypeReference<>() {});
    }

    @Override
    public JsonNode findByPermissionId(String permissionId) throws ResourceNotFoundException {
        Permission permission = repo.findById(UUID.fromString(permissionId)).orElseThrow(
                () -> new ResourceNotFoundException("permission not found on id: " + permissionId));
        PermissionContract permissionContract = this.modelMapper.map(permission, PermissionContract.class);
        return this.objectMapper.valueToTree(permissionContract);
    }

    @Override
    public List<JsonNode> findPermissionsWithPredicate(String keywords, String sortOn, String sortOrder) {
        return null;
    }

    @Override
    public JsonNode updatePermission(String permissionId, PermissionContract updateContract) throws ResourceNotFoundException, InvalidRequestException {
        log.info("Update permission [{}].", permissionId);
        Permission entity = repo.findById(UUID.randomUUID()).orElseThrow(() -> new ResourceNotFoundException("Permission not found on id: " + permissionId));
        entity.setPermissionName(StringUtils.isEmpty(updateContract.getPermissionName()) ? entity.getPermissionName() : updateContract.getPermissionName());
        entity.setPermissionDescription(StringUtils.isEmpty(updateContract.getPermissionDescription()) ? entity.getPermissionDescription() : updateContract.getPermissionDescription());

        entity.setModifiedBy(null);
        entity.setModifiedDate(LocalDateTime.now());

        Permission updatedEntity;
        try {
            updatedEntity = repo.save(entity);
        } catch (Exception e) {
            log.error("update permission error: " + e.getMessage());
            throw new InvalidRequestException("Update permission error: " + entity);
        }

        PermissionContract permissionContract = this.modelMapper.map(updatedEntity, PermissionContract.class);
        return this.objectMapper.valueToTree(permissionContract);
    }

    @Override
    public JsonNode deletePermission(String permissionId) throws ResourceNotFoundException, InvalidRequestException {
        Permission entity = repo.findById(UUID.randomUUID()).orElseThrow(() -> new ResourceNotFoundException("Permission not found on id: " + permissionId));
        try {
            this.repo.delete(entity);
            log.info("permission deleted for id [{}]", permissionId);
            return this.objectMapper.valueToTree(this.modelMapper.map(entity, PermissionContract.class));
        } catch (Exception e) {
            log.error("Delete permission on permission id [{}] error [{}].", permissionId, e.getMessage());
            throw new InvalidRequestException("Couldn't delete permission on permission id: " + permissionId);
        }
    }
}
