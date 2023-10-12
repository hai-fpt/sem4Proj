package com.lms.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.lms.dto.ApprovalStatus;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
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
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
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
    @CreationTimestamp
    private LocalDateTime fromDate;

    @Column(name = "to_date")
    @CreationTimestamp
    private LocalDateTime toDate;

    //Status: 1: Pending, 2: Approved, 3: Rejected, 4: Cancelled
    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;

    private String reason;

//    @Type(type = "list-array")
    @Type(type = "jsonb")
    @Column(name = "inform_to", columnDefinition = "jsonb")
    private List<Long> informTo;

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @Column(name = "updated_by")
    private String updatedBy;

    @OneToMany(mappedBy = "userLeave")
    private List<LeaveApproval> leaveApprovals = new ArrayList<>();

    @OneToMany(mappedBy = "leaveRequest")
    private List<FileStorage> attachedFiles = new ArrayList<>();

    @OneToMany(mappedBy = "leaveRequest")
    private List<LeaveComment> leaveComments = new ArrayList<>();

    @Transient
    private long daysOff;
}
