package com.zab.mmal.api.dtos;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CartProductVo implements Serializable {
    private static final long serialVersionUID = -6937370442493663063L;
    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer quantity;
    private String productName;
    private String productSubtitle;
    private String productMainImage;
    private BigDecimal productPrice;
    private Integer productStatus;
    private BigDecimal productTotalPrice;
    private Integer productStock;
    private Integer productChecked;//此商品是否被选中
    private String limitQuantity;//限制数量的一个返回结果

}
