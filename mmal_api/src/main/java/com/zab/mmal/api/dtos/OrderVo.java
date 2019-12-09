package com.zab.mmal.api.dtos;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderVo implements Serializable {
    private static final long serialVersionUID = 4916320424110663530L;
    /**
     * 订单号
     */
    private Long orderNo;
    /**
     * 实际付款金额,单位是元,保留两位小数
     */
    private BigDecimal payment;
    /**
     * 支付类型,1-在线支付
     */
    private Integer paymentType;
    private String paymentTypeDesc;
    /**
     * 运费,单位是元
     */
    private Integer postage;
    /**
     * 订单状态:0-已取消-10-未付款，20-已付款，40-已发货，50-交易成功，60-交易关闭
     */
    private Integer status;
    private String statusDesc;
    /**
     * 支付时间
     */
    private Date paymentTime;

    /**
     * 发货时间
     */
    private Date sendTime;

    /**
     * 交易完成时间
     */
    private Date endTime;

    /**
     * 交易关闭时间
     */
    private Date closeTime;

    /**
     * 创建时间
     */
    private Date createTime;
    private String imageHost;
    private Integer shippingId;
    private String receiverName;
    /**
     * 订单明细
     */
    private List<OrderItemVo> orderItemVos;
    /**
     * 订单收获地址
     */
    private ShippingVo shippingVo;

}
