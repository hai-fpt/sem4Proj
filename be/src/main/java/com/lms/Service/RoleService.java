package com.lms.Service;

import com.lms.DTO.RoleDTO;
import com.lms.Models.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    List<Role> getAllRoles();

    Optional<Role> findRoleById(Long id);

    Role createRole(RoleDTO role);

    Role updateRole(RoleDTO role);

    void deleteRole(Long id);
}
