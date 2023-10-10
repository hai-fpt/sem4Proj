package com.lms.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "team")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "name")
    private String teamName;

    @Column(nullable = false)
    private String description;

    @JoinColumn(name = "manager_id")
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User manager;

    @Column(nullable = false, updatable = false, name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date")
    private Date updatedDate;

    @PrePersist
    private void createdDatePre() {
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }

    private String updated_by;

    @OneToMany(mappedBy = "team")
    @JsonIgnore
    private List<UserTeam> userTeams = new ArrayList<>();
}
