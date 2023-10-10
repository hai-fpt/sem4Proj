package com.lms.service;

import com.lms.dto.RoleDTO;
import com.lms.models.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    Page<Role> getAllRoles(Pageable pageable);

    Optional<Role> findRoleById(Long id);

    Role createRole(RoleDTO role);

    Role updateRole(RoleDTO role);

    void deleteRole(Long id);
}
