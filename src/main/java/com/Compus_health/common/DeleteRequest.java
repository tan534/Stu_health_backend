package com.Compus_health.common;

import java.io.Serializable;
import lombok.Data;

@Data
public class DeleteRequest implements Serializable {

    private Integer id;

    private static final long serialVersionUID = 1L;
}
