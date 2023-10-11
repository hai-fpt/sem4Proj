package com.lms.repository;

import com.lms.dto.projection.TeamProjection;
import com.lms.models.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Page<TeamProjection> findAllProjectedBy(Pageable pageable);
    TeamProjection findTeamProjectionByTeamName(String teamName);

    Team findTeamByTeamName(String teamName);
}
