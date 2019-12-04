package com.zab.mmal.common.exceptions;


import com.zab.mmal.common.utils.StringEasyUtil;

/**
 * 传入参数有误
 */
public class WrongArgumentException extends IllegalArgumentException {
    private static final long serialVersionUID = -1176504467183471141L;

    public WrongArgumentException(){
        super();
    }

    public WrongArgumentException(String msg){
        super(msg);
    }

    public WrongArgumentException(Throwable e){
        super(e);
    }

    public WrongArgumentException(String msg,Throwable e){
        super(msg,e);
    }

    public WrongArgumentException(String message, Throwable e,
                                  Object... parameters) {
        this(StringEasyUtil.injectByBrackets(message, parameters), e);
    }

    public WrongArgumentException(String message, Object... parameters) {
        this(StringEasyUtil.injectByBrackets(message, parameters));
    }

}
