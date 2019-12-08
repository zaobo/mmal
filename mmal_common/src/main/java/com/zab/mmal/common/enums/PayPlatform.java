package com.zab.mmal.common.enums;

public enum PayPlatform {

    ALIPAY(1, "支付宝");
    private int code;
    private String description;

    PayPlatform(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
