package com.lms.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "department")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @JoinColumn(name = "manager_id")
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User manager;

    @Column(nullable = false, updatable = false, name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @PrePersist
    private void createdDatePre() {
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }

    private String updated_by;

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private List<User> users = new ArrayList<>();
}
