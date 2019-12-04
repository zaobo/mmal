package com.zab.mmal.common.exceptions;


import com.zab.mmal.common.utils.StringEasyUtil;

/**
 * 无操作权限
 */
public class NoAuthException extends IllegalArgumentException {
    private static final long serialVersionUID = -1176504467183471141L;

    public NoAuthException(){
        super();
    }

    public NoAuthException(String msg){
        super(msg);
    }

    public NoAuthException(Throwable e){
        super(e);
    }

    public NoAuthException(String msg, Throwable e){
        super(msg,e);
    }

    public NoAuthException(String message, Throwable e,
                           Object... parameters) {
        this(StringEasyUtil.injectByBrackets(message, parameters), e);
    }

    public NoAuthException(String message, Object... parameters) {
        this(StringEasyUtil.injectByBrackets(message, parameters));
    }

}
