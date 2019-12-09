package com.zab.mmal.api.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zab.mmal.api.dtos.OrderProductVo;
import com.zab.mmal.api.dtos.OrderVo;
import com.zab.mmal.api.entity.MmallOrder;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zab
 * @since 2019-11-19
 */
public interface IMmallOrderService extends IService<MmallOrder> {

    OrderVo createOrder(Integer userId, Integer shippinged);

    boolean cancelOrder(Integer userId, Long orderNo);

    OrderProductVo getOrderCartProduct(Integer userId);

    OrderVo getOrderDetails(Integer userId, Long orderNo);

    Page<OrderVo> pageOrder(Integer userId, Integer pageSize, Integer pageNo);

    boolean sendGoods(Long orderNo);
}
