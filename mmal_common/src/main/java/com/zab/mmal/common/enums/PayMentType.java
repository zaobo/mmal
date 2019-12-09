package com.zab.mmal.common.enums;

import com.zab.mmal.common.exceptions.WrongArgumentException;
import com.zab.mmal.common.utils.JudgeUtil;

public enum PayMentType {

    ONLINE_PAY(1, "在线支付");
    private int code;
    private String description;

    PayMentType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static PayMentType codeOf(int code) {
        for (PayMentType payMentType : PayMentType.values()) {
            if (JudgeUtil.isDBEq(code, payMentType.code)) {
                return payMentType;
            }
        }
        throw new WrongArgumentException("没有找到对应的枚举:{}", code);
    }

}
