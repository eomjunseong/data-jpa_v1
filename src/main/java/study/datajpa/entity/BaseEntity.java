package study.datajpa.entity;


import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity {
    
    @CreatedDate //생성시 persiti --> .save() 
    @Column(updatable = false)
    private LocalDateTime createdDate;
    
    @LastModifiedDate // flush() 시에
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @Column(updatable = false) //수정못하게
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;
}