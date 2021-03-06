package com.zab.mmal.common.enums;

import com.zab.mmal.common.exceptions.WrongArgumentException;
import com.zab.mmal.common.utils.JudgeUtil;

public enum OrderSatus {
    CANCELED(0,"已取消"),
    NO_PAY(10,"未支付"),
    PAID(20,"已支付"),
    SHIPPED(40,"已发货"),
    ORDER_SUCCESS(50,"订单完成"),
    ORDER_CLOSED(60,"订单已关闭"),
    ;

    private int code;
    private String description;

    OrderSatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static OrderSatus codeOf(int code) {
        for (OrderSatus orderSatus : OrderSatus.values()) {
            if (JudgeUtil.isDBEq(code, orderSatus.code)) {
                return orderSatus;
            }
        }
        throw new WrongArgumentException("没有找到对应的枚举:{}", code);
    }
}
