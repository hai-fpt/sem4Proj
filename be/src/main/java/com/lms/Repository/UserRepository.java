package com.lms.Repository;

import com.lms.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> getUserByCreatedDateBetween(Date startDate, Date endDate, Pageable pageable);

    @Query("select ut from UserTeam ut where ut.team.id = (select ut2.team.id from UserTeam ut2 where ut2.user.id = :userId)")
    Page<User> getUserByTeam(@Param("userId") Long id, Pageable pageable);
}
