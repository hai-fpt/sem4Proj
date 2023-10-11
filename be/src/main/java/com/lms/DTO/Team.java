package com.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    private Long id;

    private String teamName;

    private String description;

    private String requestedByEmail;

    private Long managerId;

    private List<Long> userList;

}
