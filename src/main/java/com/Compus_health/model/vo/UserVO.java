package com.Compus_health.model.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class UserVO implements Serializable {

    private Integer userId;

    private String username;

    private String phone;

    private Integer gender;

    private Integer age;

    private Double height;

    private Double weight;

    private String fitGoal;

    private Double targetWeight;

    private Integer role;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
