package com.zab.mmal.common.exceptions;

import com.zab.mmal.common.utils.StringEasyUtil;

/**
 * 系统内部故障，通常是由于系统开发错误照常，必须在测试和上线中彻底排查出此异常
 */
public class ProjectException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -4333591246791106914L;

    public ProjectException(String msg) {
        super(msg);
    }

    public ProjectException(Throwable e) {
        super(e);
    }

    public ProjectException(String msg, Throwable e) {
        super(msg, e);
    }

    public ProjectException(String message, Throwable e, Object... parameters) {
        super(StringEasyUtil.injectByBrackets(message, parameters), e);
    }

    public ProjectException(String message, Object... parameters) {
        super(StringEasyUtil.injectByBrackets(message, parameters));
    }
}