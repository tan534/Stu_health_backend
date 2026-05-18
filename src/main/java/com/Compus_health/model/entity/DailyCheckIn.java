package com.Compus_health.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@TableName(value = "checkin_records")
@Data
public class DailyCheckIn implements Serializable {

    /**
     * 记录唯一标识
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 打卡日期
     */
    @TableField("checkin_date")
    private Date checkInDate;

    /**
     * 饮水量（杯数）
     */
    private Integer water;

    /**
     * 睡眠时长（小时）
     */
    private Double sleep;

    /**
     * 运动时长（分钟）
     */
    private Integer exercise;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private Date createdAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
