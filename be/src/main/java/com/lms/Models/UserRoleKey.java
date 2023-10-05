package com.lms.Models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class UserRoleKey implements Serializable {
    private Long userId;
    private Long roleId;

    public UserRoleKey() {
    }

    public UserRoleKey(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
