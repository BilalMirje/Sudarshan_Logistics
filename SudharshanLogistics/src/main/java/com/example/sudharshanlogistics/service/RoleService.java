package com.example.sudharshanlogistics.service;

import com.example.sudharshanlogistics.entity.dtos.RoleDto;
import com.example.sudharshanlogistics.entity.Permissions;
import com.example.sudharshanlogistics.entity.Privilege;
import com.example.sudharshanlogistics.entity.Role;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {

    Role createRole(RoleDto roleDto, String ipAddress);

    Page<RoleDto> getAllRoles(Pageable pageable);

    List<RoleDto> searchRoles(String roleName);

    List<Permissions> createPermissions(List<Permissions> permissions);

    Privilege createPrivilege(Privilege privilege);

    RoleDto getRoleByRoleName(String roleName);

    Role updateRole(RoleDto roleDto);

    boolean deleteRole(UUID roleId);

    Role findById(UUID roleId);
}
