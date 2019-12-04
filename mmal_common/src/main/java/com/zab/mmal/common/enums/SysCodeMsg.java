package com.zab.mmal.common.enums;

import com.zab.mmal.common.utils.JudgeUtil;

public enum SysCodeMsg {
    SUCCESS(20000, "成功"),
    FAIL(20001, "失败"),
    PASSWORD_ERROR(20002, "密码错误"),
    USERNAME_NOT_EXIST(20003, "用户名不存在"),
    USER_REGISTER_FAIL(20004, "用户注册失败"),
    MODIFY_PWD_FAIL(20006, "修改密码失败"),
    LOGIN_FAIL(20007, "登陆失败"),
    TOKEN_ERROR(20008, "token已过期或无效"),
    TOKEN_IS_NULL(20009, "token已过期或无效"),
    UPDATE_FAIL(20010, "更新失败"),

    SYS_ERROR(10000, "系统错误"),
    PARAM_IS_NULL(10001, "必填参数未填"),
    PARAM_IS_ERROR(10002, "参数不符合规定"),
    NOT_OPT_AUTH(10003, "无操作权限"),

    NO_LOGIN(30000, "未登录"),

    ;

    private final Integer code;
    private final String describe;

    private SysCodeMsg(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescribe() {
        return describe;
    }

    public boolean same(Integer code) {
        return JudgeUtil.isDBEq(code, this.code);
    }

}