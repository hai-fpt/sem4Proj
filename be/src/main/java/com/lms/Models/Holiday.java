package com.lms.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "holiday")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, name = "name")
    private String name;

    private String description;

    @Column(name = "from_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fromDate;

    @Column(name = "to_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date toDate;

    @Column(name = "created_date", nullable = false, updatable = false)
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

    @Column(name = "updated_by")
    private String updatedBy;
}
