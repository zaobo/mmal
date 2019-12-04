package com.zab.mmal.common.commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zab.mmal.common.enums.SysCodeMsg;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ReturnData implements Serializable {

    private static final long serialVersionUID = 3766693959838514231L;
    private Integer code;

    private String message;

    private Object data;

    private Boolean success;

    private String msg;

    private String file_path;

    public ReturnData() {
        this(SysCodeMsg.SUCCESS);
    }

    public ReturnData(Object obj) {
        this();
        this.data = obj;
    }

    public ReturnData(Integer code, Boolean success, String msg) {
        this.code = code;
        this.success = success;
        this.msg = msg;
    }

    public ReturnData(String msg, String file_path) {
        this.code = SysCodeMsg.SUCCESS.getCode();
        this.success = true;
        this.msg = msg;
        this.file_path = file_path;
    }

    public ReturnData(SysCodeMsg sysCodeMsg) {
        this(sysCodeMsg.getCode(), sysCodeMsg.getDescribe());
    }

    public ReturnData(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private ReturnData(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private ReturnData(Integer code, Object data) {
        this.code = code;
        this.data = data;
    }

}
