package com.Compus_health.controller;

import com.Compus_health.model.dto.checkin.CheckInSavaRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.Compus_health.common.BaseResponse;
import com.Compus_health.common.DeleteRequest;
import com.Compus_health.common.ResultUtils;
import com.Compus_health.model.dto.checkin.CheckInAddRequest;
import com.Compus_health.model.dto.checkin.CheckInQueryRequest;
import com.Compus_health.model.entity.DailyCheckIn;
import com.Compus_health.model.entity.User;
import com.Compus_health.model.vo.CheckInVO;
import com.Compus_health.service.DailyCheckInService;
import com.Compus_health.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@Api(tags = "打卡记录接口")
@RequestMapping("/checkin")
public class DailyCheckInController {

    @Resource
    private DailyCheckInService dailyCheckInService;

    @Resource
    private UserService userService;

    @ApiOperation("添加打卡记录")
    @PostMapping("/add")
    public BaseResponse<Integer> addCheckIn(@RequestBody CheckInAddRequest request) {
        Integer id = dailyCheckInService.addCheckIn(request);
        return ResultUtils.success(id);
    }

    @ApiOperation("删除打卡记录")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteCheckIn(@RequestBody DeleteRequest request) {
        Boolean result = dailyCheckInService.deleteCheckIn(request.getId());
        return ResultUtils.success(result);
    }

    @ApiOperation("更新打卡记录")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateCheckIn(@RequestBody CheckInSavaRequest request) {
        Boolean result = dailyCheckInService.updateCheckIn(request);
        return ResultUtils.success(result);
    }

    @ApiOperation("获取打卡记录")
    @GetMapping("/get")
    public BaseResponse<CheckInVO> getCheckInById(Integer id) {
        CheckInVO checkInVO = dailyCheckInService.getCheckInById(id);
        return ResultUtils.success(checkInVO);
    }

    @ApiOperation("获取今日打卡")
    @GetMapping("/today")
    public BaseResponse<DailyCheckIn> getTodayCheckIn(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        DailyCheckIn checkIn = dailyCheckInService.getTodayCheckIn(loginUser.getUserId());
        return ResultUtils.success(checkIn);
    }

    @ApiOperation("分页获取打卡记录列表")
    @GetMapping("/list/page")
    public BaseResponse<IPage<CheckInVO>> listCheckInByPage(CheckInQueryRequest request) {
        IPage<CheckInVO> page = dailyCheckInService.listCheckInByPage(request);
        return ResultUtils.success(page);
    }

    @ApiOperation("范围获取打卡记录列表")
    @GetMapping("/list/range")
    public BaseResponse<List<CheckInVO>> listCheckIns(Date startDate, Date endDate, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        List<CheckInVO> list = dailyCheckInService.listCheckIns(loginUser.getUserId(), startDate, endDate);
        return ResultUtils.success(list);
    }

    @ApiOperation("获取当月打卡数量")
    @GetMapping("/count/month")
    public BaseResponse<Integer> countCheckInsByMonth(Date startDate, Date endDate, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Integer count = dailyCheckInService.countCheckInsByMonth(loginUser.getUserId(), startDate, endDate);
        return ResultUtils.success(count);
    }
}
