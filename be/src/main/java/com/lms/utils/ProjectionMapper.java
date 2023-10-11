package com.lms.utils;

import com.lms.dto.projection.*;
import com.lms.models.*;
import com.lms.models.LeaveApproval;
import com.lms.models.Team;
import com.lms.models.User;
import com.lms.models.UserLeave;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

public class ProjectionMapper {

    public static LeaveApprovalProjection mapToLeaveApprovalProjection(LeaveApproval leaveApproval) {
        ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
        LeaveApprovalProjection projection = pf.createProjection(LeaveApprovalProjection.class, leaveApproval);
        return projection;
    }

    public static UserLeaveProjection mapToUserLeaveProjection(UserLeave userLeave) {
        ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
        UserLeaveProjection projection = pf.createProjection(UserLeaveProjection.class, userLeave);
        return projection;
    }

    public static UserProjection mapToUserProjection(User user) {
        ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
        UserProjection projection = pf.createProjection(UserProjection.class, user);
        return projection;
    }

    public static HolidayProjection mapToHolidayProjection(Holiday holiday) {
        ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
        HolidayProjection projection = pf.createProjection(HolidayProjection.class, holiday);
        return projection;
    }

    public static LeaveProjection mapToLeaveProjection(Leave leave) {
        ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
        LeaveProjection projection = pf.createProjection(LeaveProjection.class, leave);
        return projection;
    }
    public static TeamProjection mapToTeamProjection(Team team) {
        ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
        TeamProjection projection = pf.createProjection(TeamProjection.class, team);
        return projection;
    }

    public static ManagerProjection mapToManagerProjection(User manager) {
        ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
        ManagerProjection projection = pf.createProjection(ManagerProjection.class, manager);
        return projection;
    }

    public static DepartmentProjection mapToDepartmentProjection(Department department) {
        ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
        DepartmentProjection projection = pf.createProjection(DepartmentProjection.class, department);
        return projection;
    }
}
