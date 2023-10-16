package com.lms.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "avatar")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "path")
    private String path;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, name = "created_date")
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "updated_by")
    private String updatedBy;

    public Avatar(String name, String path, User user, String updatedBy) {
        this.name = name;
        this.path = path;
        this.user = user;
        this.updatedBy = updatedBy;
    }

    public Avatar(String name, String path) {
        this.name = name;
        this.path = path;
    }
}
