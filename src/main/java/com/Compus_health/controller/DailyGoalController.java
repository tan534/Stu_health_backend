package com.Compus_health.controller;

import com.Compus_health.common.BaseResponse;
import com.Compus_health.common.ResultUtils;
import com.Compus_health.model.dto.goal.DailyGoalSaveRequest;
import com.Compus_health.model.entity.DailyGoal;
import com.Compus_health.model.entity.User;
import com.Compus_health.service.DailyGoalService;
import com.Compus_health.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "每日目标接口")
@RequestMapping("/goal")
public class DailyGoalController {

    @Resource
    private DailyGoalService dailyGoalService;

    @Resource
    private UserService userService;


    @ApiOperation("添加每日目标")
    @PostMapping("/sava")
    public BaseResponse<Boolean> addGoal(@RequestBody DailyGoalSaveRequest SavaRequest,HttpServletRequest request) {
        Boolean result = dailyGoalService.saveGoal(SavaRequest, request);
        return ResultUtils.success(result);
    }


    @ApiOperation("获取每日目标")
    @GetMapping("/get")
    public BaseResponse<DailyGoal> getTodayGoal(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        DailyGoal goal = dailyGoalService.getTodayGoal(loginUser.getUserId());
        return ResultUtils.success(goal);
    }

}