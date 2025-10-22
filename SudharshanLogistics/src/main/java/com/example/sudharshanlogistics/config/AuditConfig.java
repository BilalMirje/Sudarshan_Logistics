package com.example.sudharshanlogistics.config;

import com.example.sudharshanlogistics.entity.AppUser;
import com.example.sudharshanlogistics.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {

  @Autowired
  private UserRepository userRepository;

  @Bean
  public AuditorAware<AppUser> auditorProvider() {
    return () -> {
      try {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
          return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
          String username = userDetails.getUsername();

          // Find the user by username, handle if not found (case-insensitive)
          Optional<AppUser> userOpt = userRepository.findByUsernameIgnoreCase(username);
          if (userOpt.isPresent()) {
            return userOpt;
          } else {
            // Log or handle the case where user is not found
            System.err.println("User not found for username: " + username);
            return Optional.empty();
          }
        }

        return Optional.empty();
      } catch (Exception e) {
        // Log the exception to avoid transaction failure
        System.err.println("Error in auditorProvider: " + e.getMessage());
        return Optional.empty();
      }
    };
  }
}
