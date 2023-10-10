package com.lms.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user_leave")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLeave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "leave_id")
    private Leave leave;

    @Column(name = "from_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fromDate;

    @Column(name = "to_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date toDate;

    //Status: 1: Pending, 2: Approved, 3: Rejected, 4: Cancelled
    private Integer status;

    private String reason;

    @Type(type = "list-array")
    @Column(name = "inform_to", columnDefinition = "bigint[]")
    private List<Long> informTo;

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

    @OneToMany(mappedBy = "userLeave")
    private List<LeaveApproval> leaveApprovals = new ArrayList<>();
}
