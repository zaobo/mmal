package com.zab.mmal.core.mapper;

import com.zab.mmal.api.entity.MmallCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zab
 * @since 2019-11-19
 */
public interface MmallCartMapper extends BaseMapper<MmallCart> {

    Integer getCartProductCount(Integer userId);
}
