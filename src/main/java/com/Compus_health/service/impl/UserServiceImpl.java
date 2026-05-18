package com.Compus_health.service.impl;

import static com.Compus_health.constant.UserConstant.USER_LOGIN_STATE;

import cn.hutool.core.collection.CollUtil;
import com.Compus_health.model.dto.user.UserSaveRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.Compus_health.common.ErrorCode;
import com.Compus_health.constant.CommonConstant;
import com.Compus_health.exception.BusinessException;
import com.Compus_health.mapper.UserMapper;
import com.Compus_health.model.dto.user.UserQueryRequest;
import com.Compus_health.model.entity.User;
import com.Compus_health.model.vo.UserVO;
import com.Compus_health.service.UserService;
import com.Compus_health.utils.SqlUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    public static final String SALT = "Unony";

    /**
     * 注册
     *
     * @param username
     * @param password
     * @param checkPassword
     * @return
     */
    @Override
    public long userRegister(String username, String password, String checkPassword) {
        if (StringUtils.isAnyBlank(username, password, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (username.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (password.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (!password.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        synchronized (username.intern()) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", username);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
            User user = new User();
            user.setUsername(username);
            user.setPassword(encryptPassword);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getUserId();
        }
    }


    /**
     * 登录
     *
     * @param username
     * @param password
     * @param request
     * @return
     */
    @Override
    public UserVO userLogin(String username, String password, HttpServletRequest request) {
        if (StringUtils.isAnyBlank(username, password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (username.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (password.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("password", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("user login failed, username cannot match password");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        if (request == null) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "未登录");
            }
            request = attributes.getRequest();
        }
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getUserId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return this.getById(currentUser.getUserId());
    }


    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    @Override
    public boolean isAdmin(User user) {
        return user != null && user.getRole() != null && user.getRole() == 1;
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }


    /**
     * 获取已登录用户
     *
     * @param user
     * @return
     */
    @Override
    public UserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 获取登录用户
     *
     * @param user
     * @return
     */
    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 获取登录用户列表
     *
     * @param userList
     * @return
     */
    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    /**
     * 获取查询条件
     *
     * @param userQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Integer userId = userQueryRequest.getUserId();
        String username = userQueryRequest.getUsername();
        String phone = userQueryRequest.getPhone();
        Integer gender = userQueryRequest.getGender();
        Integer age = userQueryRequest.getAge();
        Integer role = userQueryRequest.getRole();
        Integer status = userQueryRequest.getStatus();
        String fitGoal = userQueryRequest.getFitGoal();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(userId != null, "user_id", userId);
        queryWrapper.like(StringUtils.isNotBlank(username), "username", username);
        queryWrapper.eq(StringUtils.isNotBlank(phone), "phone", phone);
        queryWrapper.eq(gender != null, "gender", gender);
        queryWrapper.eq(age != null, "age", age);
        queryWrapper.eq(role != null, "role", role);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.eq(StringUtils.isNotBlank(fitGoal), "fit_goal", fitGoal);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 更新当前用户信息
     *
     * @param request
     * @return
     */
    @Override
    public Boolean updateMyUser(UserSaveRequest updateRequest, HttpServletRequest request) {
        if (updateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        int loginId = this.getLoginUser(request).getUserId();
        if(loginId==0) throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        User newUser= new User();
        BeanUtils.copyProperties(updateRequest, newUser);
        newUser.setUserId(loginId);
        String newPassword=updateRequest.getPassword();
        if(StringUtils.isNotBlank(newPassword)){
            if(newPassword.length() < 8){
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "请输入8位以上合法密码！");
            }
            newUser.setPassword(DigestUtils.md5DigestAsHex((SALT+newPassword).getBytes()));
        }
        return this.updateById(newUser);
    }
}
