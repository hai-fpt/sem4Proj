package com.lms.repository;

import com.lms.dto.projection.UserProjection;
import com.lms.models.Team;
import com.lms.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Page<UserProjection> findAllProjectedBy(Pageable pageable);
    Optional<UserProjection> findByEmail(String email);
    Page<UserProjection> getUserByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

//    @Query("select ut from UserTeam ut where ut.team.id = (select ut2.team.id from UserTeam ut2 where ut2.user.id = :userId)")
    @Query("select u from User u " +
            "join u.userTeams ut " +
            "join ut.team t " +
            "where t.manager.id = :userId")
    List<User> getUserByTeam(@Param("userId") Long id);

    @Query("select u from User u where " +
            "(lower(u.name) like %:keyword% " +
            "or lower(u.email) like %:keyword%) " +
            "or lower(u.rank) like %:keyword% " +
            "or u.id in (select ut.user.id from UserTeam ut join ut.team t " +
            "where lower(t.teamName) like %:keyword%)")
    List<User> searchUser(@Param("keyword") String keyword);

    @Query("select ut.team from UserTeam ut where ut.user.id = :userId")
    List<Team> getTeamByUser(@Param("userId") Long id);

    @Query("select r.name from UserRole ur join ur.role r where ur.user.id = :userId")
    List<com.lms.models.Role.RoleEnum> getRolesOfUser(@Param("userId") Long id);

    Optional<User> findUserByEmail(String email);
}
