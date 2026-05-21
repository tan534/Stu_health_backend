package com.Compus_health.service.impl;

import com.Compus_health.common.ErrorCode;
import com.Compus_health.exception.BusinessException;
import com.Compus_health.model.dto.goal.DailyGoalSaveRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.Compus_health.mapper.DailyGoalMapper;
import com.Compus_health.model.entity.DailyGoal;
import com.Compus_health.model.entity.User;
import com.Compus_health.service.DailyGoalService;
import com.Compus_health.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class DailyGoalServiceImpl extends ServiceImpl<DailyGoalMapper, DailyGoal> implements DailyGoalService {

    @Resource
    private UserService userService;

    /**
     * 设置目标
     *
     * @param request
     * @return
     */
    @Override
    public Boolean saveGoal(DailyGoalSaveRequest saveRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);

        DailyGoal existGoal = this.getTodayGoal(loginUser.getUserId());

        DailyGoal goal = new DailyGoal();
        goal.setUserId(loginUser.getUserId());
        goal.setWaterGoal(saveRequest.getWaterGoal());
        goal.setSleepGoal(saveRequest.getSleepGoal());
        goal.setExerciseGoal(saveRequest.getExerciseGoal());

        boolean result;
        if (existGoal != null) {
            // 如果已存在，执行更新操作
            goal.setId(existGoal.getId());
            result = this.updateById(goal);
        } else {
            // 如果不存在，执行新增操作
            result = this.save(goal);
        }

        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "设置目标失败");
        }
        return true;
    }

    /**
     * 获取每日目标
     * @param userId
     *
     **/
    @Override
    public DailyGoal getTodayGoal(Integer userId) {
        QueryWrapper<DailyGoal> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return this.getOne(queryWrapper, false);
    }
}