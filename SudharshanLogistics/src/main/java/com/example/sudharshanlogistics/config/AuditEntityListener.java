package com.example.sudharshanlogistics.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.sudharshanlogistics.entity.Audit;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Component
public class AuditEntityListener {

  private static IpAddressAuditorAware ipAddressAuditorAware;

  @Autowired
  public void init(IpAddressAuditorAware auditorAware) {
    AuditEntityListener.ipAddressAuditorAware = auditorAware;
  }

  @PrePersist
  @PreUpdate
  public void setIpAddress(Object entity) {
    if (entity instanceof Audit auditEntity) {
      String ip = ipAddressAuditorAware.getCurrentIpAddress();
      auditEntity.setIpAddress(ip);
    }
  }

}
