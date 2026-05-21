package com.Compus_health.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 每日目标类
 * @TableName daily_goals
 */
@TableName(value = "daily_goals")
@Data
public class DailyGoal implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("water_goal")
    private Integer waterGoal;

    @TableField("sleep_goal")
    private BigDecimal sleepGoal;

    @TableField("exercise_goal")
    private Integer exerciseGoal;

    @TableField("created_at")
    private Date createdAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}