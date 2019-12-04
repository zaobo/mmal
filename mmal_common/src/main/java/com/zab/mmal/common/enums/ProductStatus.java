package com.zab.mmal.common.enums;

public enum ProductStatus {
    ON_SALE(1,"在线");

    private int code;
    private String description;

    ProductStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }
}
