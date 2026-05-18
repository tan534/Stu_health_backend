package com.Compus_health.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CheckInVO implements Serializable {

    private Integer id;

    private Integer userId;

    private String userName;

    private Date checkInDate;

    private Integer water;

    private Double sleep;

    private Integer exercise;

    private Date createdAt;

    private static final long serialVersionUID = 1L;
}
