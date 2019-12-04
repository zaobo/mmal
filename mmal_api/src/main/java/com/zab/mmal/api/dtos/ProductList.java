package com.zab.mmal.api.dtos;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductList implements Serializable {
    private static final long serialVersionUID = -8204600346848414981L;

    private Integer id;
    /**
     * 分类id,对应mmall_category表的主键
     */
    private Integer categoryId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品副标题
     */
    private String subtitle;

    /**
     * 产品主图,url相对地址
     */
    private String mainImage;

    /**
     * 价格,单位-元保留两位小数
     */
    private BigDecimal price;

    /**
     * 商品状态.1-在售 2-下架 3-删除
     */
    private Integer status;
    private String imagesHost;

}
