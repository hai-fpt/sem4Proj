package com.lms.repository;

import com.lms.models.UserRole;
import com.lms.models.UserRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleKey> {
}
