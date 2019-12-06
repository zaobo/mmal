package com.zab.mmal.api.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zab.mmal.api.entity.MmallShipping;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zab
 * @since 2019-11-19
 */
public interface IMmallShippingService extends IService<MmallShipping> {

    Integer addShipping(MmallShipping shipping);

    boolean deleteShipping(Integer userId, Integer shippingId);

    boolean updateShipping(MmallShipping shipping);

    MmallShipping getShipping(Integer userId, Integer shippingId);

    Page<MmallShipping> pageShipping(Integer userId, Integer pageSize, Integer pageNo, MmallShipping shipping);
}
