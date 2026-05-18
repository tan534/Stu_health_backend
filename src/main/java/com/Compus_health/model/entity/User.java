package com.Compus_health.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@TableName(value = "user")
@Data
public class User implements Serializable {

    /**
     * 用户id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 登录账号
     */
    private String username;

    /**
     * 角色 0-用户 1-管理员
     */
    private Integer role;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 0未知 1男 2女
     */
    private Integer gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 身高cm
     */
    @TableField("height")
    private Double height;

    /**
     * 当前体重kg
     */
    @TableField("weight")
    private Double weight;

    /**
     * 健身目标
     */
    @TableField("fit_goal")
    private String fitGoal;

    /**
     * 目标体重
     */
    @TableField("target_weight")
    private Double targetWeight;

    /**
     * 1正常 2禁用
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
