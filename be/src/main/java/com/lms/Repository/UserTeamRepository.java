package com.lms.repository;

import com.lms.models.Team;
import com.lms.models.User;
import com.lms.models.UserTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {
    UserTeam getUserTeamByUser(User user);

    Page<UserTeam> getUserTeamByTeam(Team team, Pageable pageable);

    void deleteByUser_Id(Long id);
}
