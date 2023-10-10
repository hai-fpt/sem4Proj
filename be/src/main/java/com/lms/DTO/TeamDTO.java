package com.lms.dto;

import com.lms.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamDTO {
    private Long id;

    private String teamName;

    private String description;

    private String requestedByEmail;

    private Long managerId;

    private List<Long> userList;

}
