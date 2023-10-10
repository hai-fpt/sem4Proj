package com.lms.repository;

import com.lms.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Page<User> getUserByCreatedDateBetween(Date startDate, Date endDate, Pageable pageable);

    @Query("select ut from UserTeam ut where ut.team.id = (select ut2.team.id from UserTeam ut2 where ut2.user.id = :userId)")
    Page<User> getUserByTeam(@Param("userId") Long id, Pageable pageable);

    @Query("select u from User u where " +
            "(lower(u.name) like %:keyword% " +
            "or lower(u.email) like %:keyword%) " +
            "or lower(u.rank) like %:keyword% " +
            "or u.id in (select ut.user.id from UserTeam ut join ut.team t " +
            "where lower(t.teamName) like %:keyword%)")
    Page<User> searchUser(@Param("keyword") String keyword, Pageable pageable);
}
