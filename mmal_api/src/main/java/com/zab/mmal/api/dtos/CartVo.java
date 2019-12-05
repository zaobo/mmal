package com.zab.mmal.api.dtos;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CartVo implements Serializable {
    private static final long serialVersionUID = -2858937450535706334L;

    private List<CartProductVo> cartProductVoList;
    private BigDecimal cartTotalPrice;
    private Boolean allChecked;// 是否已经都勾选
    private String imageHost;

}
