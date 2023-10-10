package com.lms.dto;

import com.lms.models.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private Long id;
    private String name;
    private String description;
    private Date createdDate;
    private Date updatedDate;
    private String updatedBy;

    private List<UserRole> userRoles = new ArrayList<>();

}
