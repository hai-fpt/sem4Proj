package com.lms.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "team")
@Getter
@Setter
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "team_name")
    private String teamName;

    @Column(nullable = false)
    private String description;

    @JoinColumn(name = "manager_id")
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User manager;

    @Column(nullable = false, updatable = false, name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @PrePersist
    private void createdDatePre() {
        this.createdDate = new Date();
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date")
    private Date updatedDate;

    private String updated_by;

    @OneToMany(mappedBy = "team")
    @JsonIgnore
    private List<UserTeam> userTeams = new ArrayList<>();
}
