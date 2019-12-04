package com.zab.mmal.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author zab
 * @since 2019-11-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MmallUser extends Model<MmallUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户表id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码，MD5加密
     */
    private String password;

    private String email;

    private String phone;

    /**
     * 找回密码问题
     */
    private String question;

    /**
     * 找回密码答案
     */
    private String answer;

    /**
     * 角色0-管理员,1-普通用户
     */
    private Integer role;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后一次更新时间
     */
    private Date updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
