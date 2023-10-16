package com.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvatarInfo {
    private String name;
    private String path;
    private String updatedBy;

    public AvatarInfo(String name, String path) {
        this.name = name;
        this.path = path;
    }

}
