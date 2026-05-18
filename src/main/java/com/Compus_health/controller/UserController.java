package com.Compus_health.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.Compus_health.annotation.AuthCheck;
import com.Compus_health.common.BaseResponse;
import com.Compus_health.common.DeleteRequest;
import com.Compus_health.common.ErrorCode;
import com.Compus_health.common.ResultUtils;
import com.Compus_health.constant.UserConstant;
import com.Compus_health.exception.BusinessException;
import com.Compus_health.exception.ThrowUtils;
import com.Compus_health.model.dto.user.UserLoginRequest;
import com.Compus_health.model.dto.user.UserQueryRequest;
import com.Compus_health.model.dto.user.UserRegisterRequest;
import com.Compus_health.model.dto.user.UserSaveRequest;
import com.Compus_health.model.entity.User;
import com.Compus_health.model.vo.UserVO;
import com.Compus_health.service.UserService;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.Compus_health.service.impl.UserServiceImpl.SALT;

@RestController
@Api("用户接口")
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation("用户注册接口")
    @PostMapping("/register")
    public BaseResponse<Integer> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String username = userRegisterRequest.getUsername();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(username, password, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(username, password, checkPassword);
        return ResultUtils.success((int) result);
    }

    @ApiOperation("用户登录接口")
    @PostMapping("/login")
    public BaseResponse<UserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String username = userLoginRequest.getUsername();
        String password = userLoginRequest.getPassword();
        if (StringUtils.isAnyBlank(username, password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVO userVO = userService.userLogin(username, password, request);
        return ResultUtils.success(userVO);
    }

    @ApiOperation("用户注销接口")
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @ApiOperation("获取当前登录用户接口")
    @GetMapping("/get/login")
    public BaseResponse<UserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(user));
    }

    @ApiOperation("创建用户接口")
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Integer> addUser(@RequestBody UserSaveRequest request, HttpServletRequest httpRequest) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(request, user);
        if (StringUtils.isBlank(request.getPassword())) {
            String defaultPassword = "12345678";
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + defaultPassword).getBytes());
            user.setPassword(encryptPassword);
        }
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getUserId());
    }

    @ApiOperation("删除用户接口")
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    @ApiOperation("更新用户接口")
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserSaveRequest request, HttpServletRequest httpRequest) {
        if (request == null || request.getUserId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(request, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @ApiOperation("根据 id 获取用户接口")
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(Integer id, HttpServletRequest request) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    @ApiOperation("根据 id 获取包装类接口")
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(Integer id, HttpServletRequest request) {
        BaseResponse<User> response = getUserById(id, request);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    @ApiOperation("分页获取用户列表接口")
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest,
            HttpServletRequest request) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        return ResultUtils.success(userPage);
    }

    @ApiOperation("分页获取用户封装列表接口")
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest,
            HttpServletRequest request) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
        List<UserVO> userVO = userService.getUserVO(userPage.getRecords());
        userVOPage.setRecords(userVO);
        return ResultUtils.success(userVOPage);
    }

    /**
     * 更新个人信息
     */
    @ApiOperation("更新个人信息接口")
    @PostMapping("/update/my")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserSaveRequest updateRequest, HttpServletRequest request) {
        Boolean result = userService.updateMyUser(updateRequest, request);
        return ResultUtils.success(result);
    }
}
