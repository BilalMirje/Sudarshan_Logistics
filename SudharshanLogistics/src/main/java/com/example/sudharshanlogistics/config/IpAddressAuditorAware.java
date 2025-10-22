package com.example.sudharshanlogistics.config;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class IpAddressAuditorAware {

  private final HttpServletRequest request;

  public IpAddressAuditorAware(HttpServletRequest request) {
    this.request = request;
  }

  public String getCurrentIpAddress() {
    // Check if we have an active request context
    ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attrs != null) {
      HttpServletRequest request = attrs.getRequest();
      return IpAddressUtil.getClientIpAddress(request);
    } else {
      // Fallback when no request context is available (e.g., during bean
      // initialization)
      return getDefaultIpAddress();
    }
  }

  private String getDefaultIpAddress() {
    try {
      // Try to get the local host IP as fallback
      return java.net.InetAddress.getLocalHost().getHostAddress();
    } catch (Exception e) {
      // If all else fails, return a default value
      return "127.0.0.1";
    }
  }
}
