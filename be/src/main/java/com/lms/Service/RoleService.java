package com.lms.service;

import com.lms.dto.Role;
import com.lms.dto.projection.RoleProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RoleService {

    Page<RoleProjection> getAllRoles(Pageable pageable);

    Optional<com.lms.models.Role> findRoleById(Long id);

    com.lms.models.Role createRole(Role role);

    com.lms.models.Role updateRole(Role role);

    void deleteRole(Long id);
}
