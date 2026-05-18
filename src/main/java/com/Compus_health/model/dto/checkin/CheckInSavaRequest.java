package com.Compus_health.model.dto.checkin;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CheckInSavaRequest implements Serializable {
    /**
     * 打卡记录的唯一标识
     */
    private Integer id;

    /**
     * 打卡日期
     */
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

    private static final long serialVersionUID = 1L;
}
