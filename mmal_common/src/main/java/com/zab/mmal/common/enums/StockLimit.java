package com.zab.mmal.common.enums;

public enum StockLimit {
    LIMIT_NUM_FAIL(0, "LIMIT_NUM_FAIL"),
    LIMIT_NUM_SUCCESS(1, "LIMIT_NUM_SUCCESS");

    private int code;
    private String description;

    StockLimit(int code, String description) {
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
