package project.doc.dmc_security_api.constants;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditTable<U> {

    @Column(name="created_by",nullable = true,updatable = false,length = 50)
    @CreatedBy
    protected U createdBy;

    @Column(name="modified_by",length = 50)
    @LastModifiedBy
    protected U modifiedBy;

    @Column(name="created_date",nullable = true,updatable = false)
    @CreatedDate
    protected LocalDateTime createdDate;

    @Column(name="modified_date",nullable = true,updatable = false)
    @LastModifiedDate
    protected LocalDateTime modifiedDate;

}
