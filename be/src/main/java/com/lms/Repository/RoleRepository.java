package com.lms.repository;

import com.lms.dto.projection.RoleProjection;
import com.lms.models.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByName(String name);

    List<Role> findByNameIn(List<Role.RoleEnum> names);

    Role findByName(Role.RoleEnum name);

    Page<RoleProjection> findAllProjectedBy(Pageable pageable);
}
