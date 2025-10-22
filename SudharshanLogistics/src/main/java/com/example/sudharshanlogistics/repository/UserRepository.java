package com.example.sudharshanlogistics.repository;

import com.example.sudharshanlogistics.entity.AppUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findByUsername(String username);

    @Query("SELECT u FROM AppUser u WHERE LOWER(u.username) = LOWER(:username)")
    Optional<AppUser> findByUsernameIgnoreCase(@Param("username") String username);

    long countByRole_Id(@Param("id") UUID roleId);

    boolean existsByUsername(String username);

    List<AppUser> findByNameContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name,
            String username, String email);

    Optional<AppUser> findByResetToken(String token);

}
