package com.lms.Repository;

import com.lms.Models.Team;
import com.lms.Models.User;
import com.lms.Models.UserTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {
    UserTeam getUserTeamByUser(User user);

    Page<UserTeam> getUserTeamByTeam(Team team, Pageable pageable);

}
