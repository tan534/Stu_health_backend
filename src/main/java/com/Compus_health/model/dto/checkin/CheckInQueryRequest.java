package com.Compus_health.model.dto.checkin;

import com.Compus_health.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class CheckInQueryRequest extends PageRequest implements Serializable {

    private Integer userId;

    private Date startDate;

    private Date endDate;

    private static final long serialVersionUID = 1L;
}
