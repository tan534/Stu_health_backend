package com.Compus_health.service;

import com.Compus_health.model.dto.checkin.CheckInSavaRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.Compus_health.model.dto.checkin.CheckInAddRequest;
import com.Compus_health.model.dto.checkin.CheckInQueryRequest;
import com.Compus_health.model.entity.DailyCheckIn;
import com.Compus_health.model.vo.CheckInVO;

import java.util.Date;
import java.util.List;

public interface DailyCheckInService extends IService<DailyCheckIn> {

    /**
     * 添加打卡记录
     *
     * @param request
     * @return
     */
    Integer addCheckIn(CheckInAddRequest request);

    /**
     * 删除打卡记录
     *
     * @param id
     * @return
     */
    Boolean deleteCheckIn(Integer id);

    /**
     * 获取打卡记录列表
     *
     * @param request
     * @return
     */
    IPage<CheckInVO> listCheckInByPage(CheckInQueryRequest request);

    /**
     * 根据id获取打卡记录
     *
     * @param id
     * @return
     */
    CheckInVO getCheckInById(Integer id);

    /**
     * 获取今日打卡
     *
     * @param userId
     * @return
     */
    DailyCheckIn getTodayCheckIn(Integer userId);

    /**
     * 获取当月打卡数量
     *
     * @param userId
     * @param startDate
     * @param endDate
     * @return
     */
    Integer countCheckInsByMonth(Integer userId, Date startDate, Date endDate);

    /**
     * 获取打卡记录列表
     *
     * @param userId
     * @param startDate
     * @param endDate
     * @return
     */
    List<CheckInVO> listCheckIns(Integer userId, Date startDate, Date endDate);

    /**
     * 修改打卡记录
     *
     * @param request
     * @return
     */
    Boolean updateCheckIn(CheckInSavaRequest request);
}
