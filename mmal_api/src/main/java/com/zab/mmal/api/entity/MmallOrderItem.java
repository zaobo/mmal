package com.zab.mmal.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author zab
 * @since 2019-11-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MmallOrderItem extends Model<MmallOrderItem> {

    private static final long serialVersionUID = 1L;

    /**
     * 订单子表id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Long orderNo;

    /**
     * 商品id
     */
    private Integer  productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品图片地址
     */
    private String productImage;

    /**
     * 生成订单时的商品单价，单位是元,保留两位小数
     */
    private BigDecimal currentUnitPrice;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 商品总价,单位是元,保留两位小数
     */
    private BigDecimal totalPrice;

    private Date createTime;

    private Date updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
