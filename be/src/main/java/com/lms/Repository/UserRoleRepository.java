package com.lms.repository;

import com.lms.dto.projection.UserRoleProjection;
import com.lms.models.User;
import com.lms.models.UserRole;
import com.lms.models.UserRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleKey> {
    @Query("select ur.role.name as name from UserRole ur where ur.user.id = :userId")
    List<UserRoleProjection> findRoleNameByUserId(@Param("userId") Long userId);

    List<UserRole> findByUser(User user);

    void deleteByUser_Id(Long id);

}
