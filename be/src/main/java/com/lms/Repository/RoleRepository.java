package com.lms.repository;

import com.lms.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByName(String name);

    List<Role> findByNameIn(List<Role.RoleEnum> names);
}
