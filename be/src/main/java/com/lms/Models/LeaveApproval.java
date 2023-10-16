package com.lms.models;

import com.lms.dto.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "leave_approval")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveApproval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_leave_id")
    private UserLeave userLeave;

    @JoinColumn(name = "manager_id")
    @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User managerId;

    //Status: 1: Pending, 2: Approved, 3: Rejected
    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;

    @Transient
    private String description;

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @Column(name = "updated_by")
    private String updatedBy;

    public LeaveApproval(UserLeave userLeave, User managerId, LocalDateTime createdDate, LocalDateTime updatedDate,
                         String updatedBy) {
        this.userLeave = userLeave;
        this.managerId = managerId;
        this.createdDate = createdDate;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
        this.status = ApprovalStatus.PENDING;
    }

}
