package com.Compus_health.model.dto.goal;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class DailyGoalSaveRequest implements Serializable {

    private Integer id;

    private Integer waterGoal;

    private BigDecimal sleepGoal;

    private Integer exerciseGoal;

    private static final long serialVersionUID = 1L;
}