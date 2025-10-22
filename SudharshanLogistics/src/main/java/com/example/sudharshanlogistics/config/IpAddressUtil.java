package com.example.sudharshanlogistics.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.regex.Pattern;

public class IpAddressUtil {

  private static final Logger logger = LoggerFactory.getLogger(IpAddressUtil.class);

  private static final Pattern IPV4_PATTERN = Pattern.compile(
      "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
          "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

  private static final Pattern IPV6_PATTERN = Pattern.compile(
      "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$|^::1$|^::$");

  public static String getClientIpAddress(HttpServletRequest request) {

    // 1️⃣ Custom Swagger header first
    String clientIp = request.getHeader("X-Client-IP");
    if (StringUtils.hasText(clientIp) && isValidIpAddress(clientIp)) {
      logger.debug("Found valid IP in X-Client-IP: {}", clientIp);
      return normalizeLocalhost(clientIp);
    }

    // 2️⃣ Check standard forwarded headers
    String[] headers = {
        "X-Forwarded-For",
        "X-Real-IP",
        "CF-Connecting-IP",
        "True-Client-IP",
        "WL-Proxy-Client-IP",
        "Proxy-Client-IP",
        "HTTP_CLIENT_IP",
        "HTTP_X_FORWARDED_FOR",
        "Forwarded",
        "X-Forwarded"
    };

    for (String header : headers) {
      String ip = request.getHeader(header);
      if (!StringUtils.hasText(ip))
        continue;

      // If header has multiple IPs, take the first valid one
      for (String part : ip.split(",")) {
        String trimmed = part.trim();
        if (isValidIpAddress(trimmed)) {
          logger.debug("Found valid IP in {}: {}", header, trimmed);
          return normalizeLocalhost(trimmed);
        }
      }

      // Special handling for Forwarded or X-Forwarded headers like "for=192.168.1.1"
      if (header.equalsIgnoreCase("Forwarded") || header.equalsIgnoreCase("X-Forwarded")) {
        String[] parts = ip.split(";");
        for (String part : parts) {
          if (part.trim().startsWith("for=")) {
            String extracted = part.trim().substring(4).replaceAll("\"", "");
            if (isValidIpAddress(extracted)) {
              logger.debug("Found valid IP in {}: {}", header, extracted);
              return normalizeLocalhost(extracted);
            }
          }
        }
      }
    }

    // 3️⃣ Fallback to remote address
    String remoteAddr = request.getRemoteAddr();
    if (StringUtils.hasText(remoteAddr) && isValidIpAddress(remoteAddr)) {
      logger.debug("Using remote address: {}", remoteAddr);
      return normalizeLocalhost(remoteAddr);
    }

    // 4️⃣ If nothing found
    logger.warn("Could not determine client IP. Remote: {}, Headers: {}",
        request.getRemoteAddr(), request.getHeaderNames());
    return "unknown";
  }

  // Normalize localhost to actual LAN IP
  private static String normalizeLocalhost(String ip) {
    if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip) ||
        "localhost".equalsIgnoreCase(ip) || "127.0.0.1".equals(ip)) {
      try {
        InetAddress inetAddress = InetAddress.getLocalHost();
        String localIp = inetAddress.getHostAddress(); // returns LAN IP like 192.168.1.50
        logger.debug("Normalized localhost to LAN IP: {}", localIp);
        return localIp;
      } catch (Exception e) {
        logger.error("Failed to get LAN IP, fallback to 127.0.0.1", e);
        return "127.0.0.1";
      }
    }
    return ip;
  }

  // Validate IPv4, IPv6, or localhost
  private static boolean isValidIpAddress(String ip) {
    if (!StringUtils.hasText(ip))
      return false;
    String trimmed = ip.trim();
    return IPV4_PATTERN.matcher(trimmed).matches()
        || IPV6_PATTERN.matcher(trimmed).matches()
        || "127.0.0.1".equals(trimmed)
        || "localhost".equalsIgnoreCase(trimmed);
  }
}
