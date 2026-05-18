package com.Compus_health.service;

import com.Compus_health.model.dto.user.UserSaveRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.Compus_health.model.dto.user.UserQueryRequest;
import com.Compus_health.model.entity.User;
import com.Compus_health.model.vo.UserVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public interface UserService extends IService<User> {

    /**
     * 用户注册
     */
    long userRegister(String username, String password, String checkPassword);

    /**
     * 用户登录
     */
    UserVO userLogin(String username, String password, HttpServletRequest request);

    /**
     * 获取当前登录用户
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 是否为管理员
     */
    boolean isAdmin(User user);

    /**
     * 用户注销
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏的已登录用户信息
     */
    UserVO getLoginUserVO(User user);

    /**
     * 获取脱敏的用户信息
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏的用户信息
     */
    List<UserVO> getUserVO(List<User> userList);

    /**
     * 获取查询条件
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 更新当前登录用户信息
     */
    Boolean updateMyUser(UserSaveRequest updaterequest, HttpServletRequest request);
}
