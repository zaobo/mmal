package com.zab.mmal.common.exceptions;


import com.zab.mmal.common.utils.StringEasyUtil;

/**
 * 数据库数据有误
 */
public class WrongDataException extends IllegalArgumentException {
    private static final long serialVersionUID = -1176504467183471141L;

    public WrongDataException(){
        super();
    }

    public WrongDataException(String msg){
        super(msg);
    }

    public WrongDataException(Throwable e){
        super(e);
    }

    public WrongDataException(String msg, Throwable e){
        super(msg,e);
    }

    public WrongDataException(String message, Throwable e,
                              Object... parameters) {
        this(StringEasyUtil.injectByBrackets(message, parameters), e);
    }

    public WrongDataException(String message, Object... parameters) {
        this(StringEasyUtil.injectByBrackets(message, parameters));
    }

}
