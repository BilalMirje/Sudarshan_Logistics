package com.example.sudharshanlogistics.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.sudharshanlogistics.config.AuditEntityListener;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EntityListeners({ AuditingEntityListener.class, AuditEntityListener.class })
@MappedSuperclass
public abstract class Audit {

  @CreatedDate
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @CreatedBy
  @ManyToOne
  @JoinColumn(name = "created_by")
  @JsonBackReference
  private AppUser createdBy;

  @LastModifiedBy
  @ManyToOne
  @JoinColumn(name = "updated_by")
  @JsonBackReference
  private AppUser updatedBy;

  @Column(name = "ip_address")
  private String ipAddress;
}
