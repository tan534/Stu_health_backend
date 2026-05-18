package com.Compus_health.service.impl;

import cn.hutool.core.date.DateUtil;
import com.Compus_health.model.dto.checkin.CheckInSavaRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.Compus_health.mapper.DailyCheckInMapper;
import com.Compus_health.mapper.UserMapper;
import com.Compus_health.model.dto.checkin.CheckInAddRequest;
import com.Compus_health.model.dto.checkin.CheckInQueryRequest;
import com.Compus_health.model.entity.DailyCheckIn;
import com.Compus_health.model.entity.User;
import com.Compus_health.model.vo.CheckInVO;
import com.Compus_health.service.DailyCheckInService;
import com.Compus_health.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DailyCheckInServiceImpl extends ServiceImpl<DailyCheckInMapper, DailyCheckIn> implements DailyCheckInService {

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    /**
     * 添加打卡记录
     *
     * @param request
     * @return
     */
    @Override
    public Integer addCheckIn(CheckInAddRequest request) {
        User loginUser = userService.getLoginUser(null);

        Date today = DateUtil.beginOfDay(new Date());
        QueryWrapper<DailyCheckIn> todayQueryWrapper = new QueryWrapper<>();
        todayQueryWrapper.eq("user_id", loginUser.getUserId());
        todayQueryWrapper.eq("checkin_date", today);
        DailyCheckIn existingCheckIn = this.getOne(todayQueryWrapper, false);
        if (existingCheckIn != null) {
            throw new RuntimeException("今日已打卡");
        }

        DailyCheckIn checkIn = new DailyCheckIn();
        checkIn.setUserId(loginUser.getUserId());
        checkIn.setCheckInDate(today);
        checkIn.setWater(request.getWater());
        checkIn.setSleep(request.getSleep());
        checkIn.setExercise(request.getExercise());

        boolean saveResult = this.save(checkIn);
        if (!saveResult) {
            throw new RuntimeException("打卡失败");
        }
        return checkIn.getId();
    }

    /**
     * 删除打卡记录
     *
     * @param id
     * @return
     */
    @Override
    public Boolean deleteCheckIn(Integer id) {
        DailyCheckIn checkIn = this.getById(id);
        if (checkIn == null) {
            throw new RuntimeException("打卡记录不存在");
        }
        User loginUser = userService.getLoginUser(null);
        if (!checkIn.getUserId().equals(loginUser.getUserId()) && !userService.isAdmin(loginUser)) {
            throw new RuntimeException("无权限删除");
        }
        return this.removeById(id);
    }

    /**
     * 更新打卡记录
     *
     * @param request
     * @return
     */
    @Override
    public Boolean updateCheckIn(CheckInSavaRequest request) {
        DailyCheckIn checkIn = this.getById(request.getId());
        if (checkIn == null) {
            throw new RuntimeException("打卡记录不存在");
        }
        User loginUser = userService.getLoginUser(null);
        if (!checkIn.getUserId().equals(loginUser.getUserId()) && !userService.isAdmin(loginUser)) {
            throw new RuntimeException("无权限更新非本人打卡记录");
        }

        checkIn.setCheckInDate(request.getCheckInDate());
        checkIn.setWater(request.getWater());
        checkIn.setSleep(request.getSleep());
        checkIn.setExercise(request.getExercise());
        return this.updateById(checkIn);
    }

    /**
     * 获取打卡记录分页列表
     *
     * @param request
     * @return
     */
    public IPage<CheckInVO> listCheckInByPage(CheckInQueryRequest request) {
        User loginUser = userService.getLoginUser(null);
        Integer userId = request.getUserId();
        if (userId == null) {
            userId = loginUser.getUserId();
        }
        if (!userId.equals(loginUser.getUserId()) && !userService.isAdmin(loginUser)) {
            throw new RuntimeException("无权限查看");
        }

        Page<DailyCheckIn> page = new Page<>(request.getCurrent(), request.getPageSize());
        QueryWrapper<DailyCheckIn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.ge(request.getStartDate() != null, "checkin_date", request.getStartDate());
        queryWrapper.le(request.getEndDate() != null, "checkin_date", request.getEndDate());
        queryWrapper.orderByDesc("checkin_date");
        IPage<DailyCheckIn> checkInPage = this.page(page, queryWrapper);

        return checkInPage.convert(this::getCheckInVO);
    }

    /**
     * 根据id获取打卡记录
     *
     * @param id
     * @return
     */
    public CheckInVO getCheckInById(Integer id) {
        DailyCheckIn checkIn = this.getById(id);
        if (checkIn == null) {
            return null;
        }
        return getCheckInVO(checkIn);
    }

    /**
     * 获取当前用户今日打卡记录
     *
     * @param userId
     * @return
     */
    public DailyCheckIn getTodayCheckIn(Integer userId) {
        Date today = DateUtil.beginOfDay(new Date());
        QueryWrapper<DailyCheckIn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("checkin_date", today);
        return this.getOne(queryWrapper, false);
    }

    /**
     * 获取当月打卡记录数量
     *
     * @param userId
     * @param startDate
     * @param endDate
     * @return
     */
    public Integer countCheckInsByMonth(Integer userId, Date startDate, Date endDate) {
        QueryWrapper<DailyCheckIn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.between("checkin_date", startDate, endDate);
        return Math.toIntExact(this.count(queryWrapper));
    }

    /**
     * 范围获取打卡记录列表
     *
     * @param userId
     * @param startDate
     * @param endDate
     * @return
     */
    public List<CheckInVO> listCheckIns(Integer userId, Date startDate, Date endDate) {
        QueryWrapper<DailyCheckIn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.ge(startDate != null, "checkin_date", startDate);
        queryWrapper.le(endDate != null, "checkin_date", endDate);
        queryWrapper.orderByDesc("checkin_date");
        List<DailyCheckIn> checkInList = this.list(queryWrapper);
        return checkInList.stream().map(this::getCheckInVO).collect(Collectors.toList());
    }

    /**
     * 获取打卡记录
     *
     * @param checkIn
     * @return
     */
    private CheckInVO getCheckInVO(DailyCheckIn checkIn) {
        CheckInVO vo = new CheckInVO();
        BeanUtils.copyProperties(checkIn, vo);
        vo.setId(checkIn.getId());
        User user = userMapper.selectById(checkIn.getUserId());
        if (user != null) {
            vo.setUserName(user.getUsername());
        }
        return vo;
    }
}
