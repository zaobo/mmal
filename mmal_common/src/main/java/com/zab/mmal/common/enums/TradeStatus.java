package com.zab.mmal.common.enums;

public enum  TradeStatus {
    WAIT_BUYER_PAY(1,"交易创建，等待买家付款"),
    TRADE_CLOSED(-1,"未付款交易超时关闭，或支付完成后全额退款"),
    TRADE_SUCCESS(0,"交易支付成功"),
    TRADE_FINISHED(2,"交易结束");

    private int code;
    private String desc;

    TradeStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
