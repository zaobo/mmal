package com.zab.mmal.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zab.mmal.api.entity.MmallOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zab
 * @since 2019-11-19
 */
public interface MmallOrderItemMapper extends BaseMapper<MmallOrderItem> {

    void batchInsert(@Param("orderItemList") List<MmallOrderItem> orderItemList);

}
