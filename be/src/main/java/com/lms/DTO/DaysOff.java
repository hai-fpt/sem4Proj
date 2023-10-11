package com.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DaysOff {
    float defaultDaysOff;
    float usedDaysOff;
    float remainingDaysOff;
}
