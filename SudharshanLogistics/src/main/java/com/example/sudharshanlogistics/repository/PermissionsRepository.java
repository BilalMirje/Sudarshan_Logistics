package com.example.sudharshanlogistics.repository;

import com.example.sudharshanlogistics.entity.Permissions;
import com.example.sudharshanlogistics.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface PermissionsRepository extends JpaRepository<Permissions, UUID> {
    Collection<Object> getPermissionsByRole(Role savedRole);

    List<Permissions> getPermissionsByRole_Id(UUID roleId);
}
