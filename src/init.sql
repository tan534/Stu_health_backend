-- 创建数据库
CREATE DATABASE IF NOT EXISTS student_health_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE student_health_system;

-- 1. 用户表
CREATE TABLE `user` (
     `user_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户id',
     `username` VARCHAR(30) NOT NULL COMMENT '登录账号',
     `role`  TINYINT DEFAULT 0 COMMENT '角色 0-用户 1-管理员',
     `password` VARCHAR(60) NOT NULL COMMENT '密码',
     `phone` VARCHAR(11) DEFAULT NULL COMMENT '手机号',
     `gender` TINYINT DEFAULT 0 COMMENT '0未知 1男 2女',
     `age` INT DEFAULT 0 COMMENT '年龄',
     `height` DECIMAL(5,1) DEFAULT 0 COMMENT '身高cm',
     `weight` DECIMAL(5,1) DEFAULT 0 COMMENT '当前体重kg',
     `fit_goal` VARCHAR(20) DEFAULT '' COMMENT '健身目标',
     `target_weight` DECIMAL(5,1) DEFAULT 0 COMMENT '目标体重',
     `reminder_water` TIME DEFAULT '09:00:00',
     `reminder_sleep` TIME DEFAULT '23:00:00',
     `reminder_exercise` TIME DEFAULT '19:00:00',
     `status` TINYINT DEFAULT 1 COMMENT '1正常 2禁用',
     `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
     `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 打卡记录表
CREATE TABLE IF NOT EXISTS checkin_records (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '记录唯一标识',
    user_id INT NOT NULL COMMENT '用户ID',
    checkin_date DATE NOT NULL COMMENT '打卡日期',
    water INT DEFAULT 0 COMMENT '饮水量（杯数）',
    sleep DECIMAL(4,1) DEFAULT 0 COMMENT '睡眠时长（小时）',
    exercise INT DEFAULT 0 COMMENT '运动时长（分钟）',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_date (user_id, checkin_date),
    INDEX idx_user_id (user_id),
    INDEX idx_checkin_date (checkin_date)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='打卡记录表';

-- 3. 每日目标表
CREATE TABLE IF NOT EXISTS daily_goals (
     id INT PRIMARY KEY AUTO_INCREMENT COMMENT '目标ID',
     user_id INT NOT NULL COMMENT '用户ID',
     water_goal INT DEFAULT 0 COMMENT '喝水目标（杯数）',
     sleep_goal DECIMAL(4,1) DEFAULT 0 COMMENT '睡眠目标（小时）',
    exercise_goal INT DEFAULT 0 COMMENT '运动目标（分钟）',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_id (user_id),
    INDEX idx_user_id (user_id)
    ) Engine=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='每日目标表';


