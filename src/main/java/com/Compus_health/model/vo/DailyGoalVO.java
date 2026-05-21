package com.Compus_health.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class DailyGoalVO implements Serializable {

    private Integer id;

    private Integer userId;

    private String userName;

    private Integer waterGoal;

    private BigDecimal sleepGoal;

    private Integer exerciseGoal;

    private Date createdAt;

    private static final long serialVersionUID = 1L;
}