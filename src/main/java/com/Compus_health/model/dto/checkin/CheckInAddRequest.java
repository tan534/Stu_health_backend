package com.Compus_health.model.dto.checkin;

import lombok.Data;

import java.io.Serializable;

@Data
public class CheckInAddRequest implements Serializable {

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
