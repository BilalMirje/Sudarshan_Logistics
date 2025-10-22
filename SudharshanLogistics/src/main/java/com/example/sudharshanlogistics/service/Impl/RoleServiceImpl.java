package com.example.sudharshanlogistics.service.Impl;

import com.example.sudharshanlogistics.entity.dtos.PermissionDto;
import com.example.sudharshanlogistics.entity.dtos.RoleDto;
import com.example.sudharshanlogistics.entity.Permissions;
import com.example.sudharshanlogistics.entity.Privilege;
import com.example.sudharshanlogistics.entity.Role;
import com.example.sudharshanlogistics.repository.PermissionsRepository;
import com.example.sudharshanlogistics.repository.PrivilegeRepository;
import com.example.sudharshanlogistics.repository.RoleRepository;
import com.example.sudharshanlogistics.repository.UserRepository;
import com.example.sudharshanlogistics.service.RoleService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionsRepository permissionRepository;
    private final PrivilegeRepository privilegeRepository;
    private final UserRepository appUserRepository;

    @Override
    public Role createRole(RoleDto roleDto, String ipAddress) {

        List<String> validPrivilegesList = List.of("READ", "WRITE", "UPDATE", "DELETE");

        if (roleRepository.findByRoleName(roleDto.getRoleName()).isPresent()) {
            return null;
        }

        Role role = new Role();
        role.setRoleName(roleDto.getRoleName());
        role.setRoleDescription(roleDto.getRoleDescription());
        role.setIpAddress(ipAddress); // Set the IP address

        List<Permissions> permissions = new ArrayList<>();

        for (PermissionDto permissionDto : roleDto.getPermissions()) {
            Permissions permission = new Permissions();
            permission.setUserPermission(permissionDto.getUserPermission());

            Privilege privilege = new Privilege();

            for (String prg : permissionDto.getPrivileges()) {
                if (validPrivilegesList.contains(prg)) {
                    switch (prg) {
                        case "READ" -> privilege.setReadPermission("READ");
                        case "WRITE" -> privilege.setWritePermission("WRITE");
                        case "UPDATE" -> privilege.setUpdatePermission("UPDATE");
                        case "DELETE" -> privilege.setDeletePermission("DELETE");
                    }
                }
            }
            permission.setPrivilege(privilege);
            permissions.add(permission);
        }

        role.setPermissions(permissions);
        return roleRepository.save(role);
    }

    @Override
    public Page<RoleDto> getAllRoles(Pageable pageable) {
        Page<Role> rolesPage = roleRepository.findAll(pageable);

        return rolesPage.map(role -> {
            RoleDto roleDto = new RoleDto();
            roleDto.setRoleId(role.getId());
            roleDto.setRoleName(role.getRoleName());
            roleDto.setRoleDescription(role.getRoleDescription());

            List<PermissionDto> permissionDtos = role.getPermissions().stream().map(permission -> {
                PermissionDto permissionDto = new PermissionDto();
                permissionDto.setUserPermission(permission.getUserPermission());

                List<String> privileges = new ArrayList<>();
                privileges.add(permission.getPrivilege().getReadPermission());
                privileges.add(permission.getPrivilege().getWritePermission());
                privileges.add(permission.getPrivilege().getUpdatePermission());
                privileges.add(permission.getPrivilege().getDeletePermission());

                permissionDto.setPrivileges(privileges);
                return permissionDto;
            }).toList();

            roleDto.setPermissions(permissionDtos);

            return roleDto;
        });
    }

    @Override
    public List<RoleDto> searchRoles(String roleName) {
        List<Role> roles = roleRepository.findByRoleNameContainingIgnoreCase(roleName);
        return roles.stream().map(role -> {
            RoleDto roleDto = new RoleDto();
            roleDto.setRoleId(role.getId());
            roleDto.setRoleName(role.getRoleName());
            roleDto.setRoleDescription(role.getRoleDescription());

            List<PermissionDto> permissionDtos = role.getPermissions().stream().map(permission -> {
                PermissionDto permissionDto = new PermissionDto();
                permissionDto.setUserPermission(permission.getUserPermission());

                List<String> privileges = new ArrayList<>();
                privileges.add(permission.getPrivilege().getReadPermission());
                privileges.add(permission.getPrivilege().getWritePermission());
                privileges.add(permission.getPrivilege().getUpdatePermission());
                privileges.add(permission.getPrivilege().getDeletePermission());

                permissionDto.setPrivileges(privileges);
                return permissionDto;
            }).toList();

            roleDto.setPermissions(permissionDtos);

            return roleDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Permissions> createPermissions(List<Permissions> permissions) {
        return permissionRepository.saveAll(permissions);
    }

    @Override
    public Privilege createPrivilege(Privilege privilege) {
        return privilegeRepository.save(privilege);
    }

    @Override
    public RoleDto getRoleByRoleName(String roleName) {
        Optional<Role> role = roleRepository.findByRoleName(roleName);
        if (role.isEmpty()) {
            return null;
        }

        RoleDto roleDto = new RoleDto();

        roleDto.setRoleId(role.get().getId());
        roleDto.setRoleName(role.get().getRoleName());
        roleDto.setRoleDescription(role.get().getRoleDescription());

        List<PermissionDto> permissionDtos = new ArrayList<>();
        for (Permissions permission : role.get().getPermissions()) {
            PermissionDto permissionDto = new PermissionDto();
            List<String> privileges = new ArrayList<>();
            permissionDto.setUserPermission(permission.getUserPermission());
            privileges.add(permission.getPrivilege().getReadPermission());
            privileges.add(permission.getPrivilege().getWritePermission());
            privileges.add(permission.getPrivilege().getUpdatePermission());
            privileges.add(permission.getPrivilege().getDeletePermission());

            permissionDto.setPrivileges(privileges);
            permissionDtos.add(permissionDto);
        }

        roleDto.setPermissions(permissionDtos);
        return roleDto;
    }

    @Override
    public Role updateRole(RoleDto roleDto) {

        List<String> validPrivilegesList = List.of("READ", "WRITE", "UPDATE", "DELETE");

        Optional<Role> existedRole = roleRepository.findById(roleDto.getRoleId());

        existedRole.get().setRoleName(roleDto.getRoleName());
        existedRole.get().setRoleDescription(roleDto.getRoleDescription());

        if (existedRole.get().getPermissions() != null) {
            existedRole.get().getPermissions().clear();

            List<Permissions> permissions = new ArrayList<>();
            for (PermissionDto prms : roleDto.getPermissions()) {
                Permissions permission = new Permissions();
                permission.setUserPermission(prms.getUserPermission());

                Privilege privilege = new Privilege();

                for (String prg : prms.getPrivileges()) {
                    if (validPrivilegesList.contains(prg)) {
                        switch (prg) {
                            case "READ" -> privilege.setReadPermission("READ");
                            case "WRITE" -> privilege.setWritePermission("WRITE");
                            case "UPDATE" -> privilege.setUpdatePermission("UPDATE");
                            case "DELETE" -> privilege.setDeletePermission("DELETE");
                        }
                    }
                }
                permission.setPrivilege(privilege);

                permissions.add(permission);

            }
            existedRole.get().getPermissions().addAll(permissions);
        } else {
            List<Permissions> permissions = new ArrayList<>();
            for (PermissionDto prms : roleDto.getPermissions()) {
                Permissions permission = new Permissions();
                permission.setUserPermission(prms.getUserPermission());

                Privilege privilege = new Privilege();

                for (String prg : prms.getPrivileges()) {
                    if (validPrivilegesList.contains(prg)) {
                        switch (prg) {
                            case "READ" -> privilege.setReadPermission("READ");
                            case "WRITE" -> privilege.setWritePermission("WRITE");
                            case "UPDATE" -> privilege.setUpdatePermission("UPDATE");
                            case "DELETE" -> privilege.setDeletePermission("DELETE");
                        }
                    }
                }
                permission.setPrivilege(privilege);

                permissions.add(permission);

            }
            existedRole.get().setPermissions(permissions);
        }

        return roleRepository.save(existedRole.get());
    }

    @Override
    public boolean deleteRole(UUID roleId) {
        if (appUserRepository.countByRole_Id(roleId) > 0) {
            return false;
        }

        if (roleRepository.findById(roleId).isEmpty()) {
            return false;
        }

        List<Permissions> permissions = permissionRepository.getPermissionsByRole_Id(roleId);
        if (!permissions.isEmpty()) {
            for (Permissions permission : permissions) {
                privilegeRepository.deleteById(permission.getPrivilege().getPrivilegeId());
                permissionRepository.deleteById(permission.getId());
            }
        }
        roleRepository.deleteById(roleId);
        return true;
    }

    @Override
    public Role findById(UUID roleId) {
        return roleRepository.findById(roleId).get();
    }
}
