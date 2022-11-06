package com.example.webkit640.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public abstract class DateAudit {
    @CreatedDate
    private String createDate;

    @LastModifiedDate
    private String updateDate;

    @PrePersist
    public void onPrePersist() {
        this.createDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.updateDate = this.createDate;
    }

    @PreUpdate
    public void onPreUpdate(){
        this.updateDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
