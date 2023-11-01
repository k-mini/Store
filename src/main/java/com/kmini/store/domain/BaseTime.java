package com.kmini.store.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTime {

//    @Column(name="INSERT_MEMBER")
//    private String createdBy;
    @CreatedDate
    private LocalDateTime createdDate;

//    @Column(name="UPADATE_MEMBER")
//    private String lastModifiedBy;
    @CreatedDate
    private LocalDateTime lastModifiedDate;
}
