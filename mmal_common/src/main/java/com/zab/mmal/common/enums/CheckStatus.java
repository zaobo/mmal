package com.zab.mmal.common.enums;

public enum CheckStatus {
    CHECKED(1,"已勾选"),
    UN_CHECKED(0,"未勾选");

    private int code;
    private String description;

    CheckStatus(int code, String description) {
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
