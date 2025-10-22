package com.example.sudharshanlogistics.repository;

import com.example.sudharshanlogistics.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByRoleName(String roleName);

    List<Role> findByRoleNameContainingIgnoreCase(String name);
}
