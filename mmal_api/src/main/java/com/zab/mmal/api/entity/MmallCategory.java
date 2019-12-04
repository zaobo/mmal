package com.zab.mmal.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

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
public class MmallCategory extends Model<MmallCategory> {

    private static final long serialVersionUID = 1L;

    /**
     * 类别Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父类别id当id=0时说明是根节点,一级类别
     */
    private Integer parentId;

    /**
     * 类别名称
     */
    private String name;

    /**
     * 类别状态1-正常,2-已废弃
     */
    private Boolean status;

    /**
     * 排序编号,同类展示顺序,数值相等则自然排序
     */
    private Integer sortOrder;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MmallCategory that = (MmallCategory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
