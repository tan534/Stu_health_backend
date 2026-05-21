package com.Compus_health.service;

import com.Compus_health.model.dto.goal.DailyGoalSaveRequest;
import com.Compus_health.model.entity.DailyGoal;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface DailyGoalService extends IService<DailyGoal> {

    /**
     * 添加目标
     *
     * @param request
     * @return
     */
    Boolean saveGoal(DailyGoalSaveRequest saveRequest, HttpServletRequest request);


    /**
     * 获取今日目标
     *
     * @param userId
     * @return
     */
    DailyGoal getTodayGoal(Integer userId);

}