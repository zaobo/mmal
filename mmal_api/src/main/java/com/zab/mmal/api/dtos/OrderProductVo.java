package com.zab.mmal.api.dtos;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderProductVo implements Serializable {

    private List<OrderItemVo> orderItemVos;
    private BigDecimal productTotalPrice;
    private String imageHost;

}
