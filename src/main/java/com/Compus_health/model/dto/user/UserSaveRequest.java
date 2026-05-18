package com.Compus_health.model.dto.user;

import java.io.Serializable;
import lombok.Data;

@Data
public class UserSaveRequest implements Serializable {

    private Integer userId;

    private String username;

    private String password;

    private String phone;

    private Integer gender;

    private Integer age;

    private Double height;

    private Double weight;

    private String fitGoal;

    private Double targetWeight;

    private Integer role;

    private Integer status;

    private static final long serialVersionUID = 1L;
}
