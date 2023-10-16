package com.lms.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    private String name;

    private Long managerId;

    private List<Long> teamList;
}
