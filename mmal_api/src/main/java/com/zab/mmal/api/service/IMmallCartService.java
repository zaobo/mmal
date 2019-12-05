package com.zab.mmal.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zab.mmal.api.dtos.CartVo;
import com.zab.mmal.api.entity.MmallCart;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zab
 * @since 2019-11-19
 */
public interface IMmallCartService extends IService<MmallCart> {

    CartVo addCatr(MmallCart cart);

    CartVo updateCart(MmallCart cart);

    CartVo deleteCart(Integer userId, List<Integer> productIds);

    CartVo listCart(Integer userId);

    CartVo selectOrUnSelectCart(Integer userId, Integer checked, List<Integer> productIds);

    Integer getCartProductCount(Integer userId);
}
