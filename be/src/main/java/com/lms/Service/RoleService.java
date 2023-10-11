package com.lms.service;

import com.lms.dto.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RoleService {

    Page<com.lms.models.Role> getAllRoles(Pageable pageable);

    Optional<com.lms.models.Role> findRoleById(Long id);

    com.lms.models.Role createRole(Role role);

    com.lms.models.Role updateRole(Role role);

    void deleteRole(Long id);
}
