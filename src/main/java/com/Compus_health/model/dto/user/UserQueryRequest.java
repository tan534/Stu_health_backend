package com.Compus_health.model.dto.user;

import com.Compus_health.common.PageRequest;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {

    private Integer userId;

    private String username;

    private String phone;

    private Integer gender;

    private Integer age;

    private Integer role;

    private Integer status;

    private String fitGoal;

    private String sortField;

    private String sortOrder;

    private static final long serialVersionUID = 1L;
}
