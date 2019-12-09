package com.zab.mmal.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zab.mmal.api.dtos.OrderItemVo;
import com.zab.mmal.api.dtos.OrderProductVo;
import com.zab.mmal.api.dtos.OrderVo;
import com.zab.mmal.api.dtos.ShippingVo;
import com.zab.mmal.api.entity.*;
import com.zab.mmal.api.service.IMmallOrderService;
import com.zab.mmal.common.config.FtpConfiguration;
import com.zab.mmal.common.enums.*;
import com.zab.mmal.common.exceptions.WrongArgumentException;
import com.zab.mmal.common.exceptions.WrongDataException;
import com.zab.mmal.common.utils.BigDecimalUtil;
import com.zab.mmal.common.utils.JudgeUtil;
import com.zab.mmal.core.mapper.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zab
 * @since 2019-11-19
 */
@Service
public class MmallOrderServiceImpl extends ServiceImpl<MmallOrderMapper, MmallOrder> implements IMmallOrderService {

    @Resource
    private MmallCartMapper cartMapper;
    @Resource
    private MmallProductMapper productMapper;
    @Resource
    private MmallOrderItemMapper orderItemMapper;
    @Resource
    private MmallShippingMapper shippingMapper;
    @Resource
    private FtpConfiguration ftpConfiguration;

    @Override
    @Transactional
    public OrderVo createOrder(Integer userId, Integer shippingId) {
        QueryWrapper<MmallCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("checked", CheckStatus.CHECKED.getCode());
        List<MmallCart> cartList = cartMapper.selectList(queryWrapper);
        if (JudgeUtil.isEmpty(cartList)) {
            throw new WrongDataException("购物车为空:{}", userId);
        }

        // 生成一个订单号
        long orderNo = this.generatorOrderNo();
        List<MmallOrderItem> orderItemList = this.getCartOrderIterms(userId, orderNo, cartList);
        if (JudgeUtil.isEmpty(orderItemList)) {
            throw new WrongDataException("购物车为空:{}", userId);
        }
        // 计算总价
        BigDecimal payment = new BigDecimal("0");
        for (MmallOrderItem orderItem : orderItemList) {
            payment = BigDecimalUtil.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }

        // 生成订单
        MmallOrder order = this.assebleOrder(userId, shippingId, orderNo, payment);
        if (null == order) {
            throw new WrongArgumentException("生成订单失败:{}", userId);
        }

        // 插入订单明细
        orderItemMapper.batchInsert(orderItemList);
        // 生成成功后，减少产品的库存
        this.reduceProductStock(orderItemList);
        // 清空购物车
        this.clearCart(cartList);

        // 返给前端订单信息，地址等
        return this.assembleOrderVo(order, orderItemList);
    }

    @Override
    @Transactional
    public boolean cancelOrder(Integer userId, Long orderNo) {
        QueryWrapper<MmallOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("order_no", orderNo);
        MmallOrder order = getOne(queryWrapper);
        if (null == order) {
            throw new WrongArgumentException("用户:{}的订单:{}不存在", userId, orderNo);
        }

        if (JudgeUtil.isDBEq(order.getStatus(), OrderSatus.NO_PAY.getCode())) {
            throw new WrongArgumentException("订单已付款，无法取消订单:{}", orderNo);
        }

        MmallOrder updateOrder = new MmallOrder();
        updateOrder.setStatus(OrderSatus.CANCELED.getCode());
        updateOrder.setId(order.getId());
        return updateById(updateOrder);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public OrderProductVo getOrderCartProduct(Integer userId) {
        QueryWrapper<MmallCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("checked", CheckStatus.CHECKED.getCode());
        List<MmallCart> cartList = cartMapper.selectList(queryWrapper);
        if (JudgeUtil.isEmpty(cartList)) {
            throw new WrongDataException("购物车为空:{}", userId);
        }

        List<MmallOrderItem> orderItemList = this.getCartOrderIterms(userId, null, cartList);
        BigDecimal productTotalPrice = new BigDecimal("0");
        List<OrderItemVo> orderItemVos = new LinkedList<>();
        for (MmallOrderItem orderItem : orderItemList) {
            productTotalPrice = BigDecimalUtil.add(productTotalPrice.doubleValue(), orderItem.getTotalPrice().doubleValue());
            orderItemVos.add(assembleOrderItemVo(orderItem));
        }

        OrderProductVo orderProductVo = new OrderProductVo();
        orderProductVo.setOrderItemVos(orderItemVos);
        orderProductVo.setProductTotalPrice(productTotalPrice);
        orderProductVo.setImageHost(ftpConfiguration.getServerHttpPrefix());
        return orderProductVo;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public OrderVo getOrderDetails(Integer userId, Long orderNo) {
        QueryWrapper<MmallOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("order_no", orderNo);
        MmallOrder order = getOne(queryWrapper);
        if (null == order) {
            throw new WrongArgumentException("用户:{}的订单:{}不存在", userId, orderNo);
        }

        // 查询订单明细
        QueryWrapper<MmallOrderItem> queryOrderItem = new QueryWrapper<>();
        queryOrderItem.eq("user_id", userId);
        queryOrderItem.eq("order_no", orderNo);
        List<MmallOrderItem> orderItemList = orderItemMapper.selectList(queryOrderItem);

        return assembleOrderVo(order, orderItemList);
    }

    private OrderVo assembleOrderVo(MmallOrder order, List<MmallOrderItem> orderItemList) {
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        orderVo.setPaymentTypeDesc(PayMentType.codeOf(order.getPaymentType()).getDescription());
        orderVo.setPostage(order.getPostage());
        orderVo.setStatus(order.getStatus());
        orderVo.setStatusDesc(OrderSatus.codeOf(order.getStatus()).getDescription());

        orderVo.setShippingId(order.getShippingId());
        // 收获地址
        MmallShipping shipping = shippingMapper.selectById(order.getShippingId());
        if (null != shipping) {
            orderVo.setReceiverName(shipping.getReceiverName());
            ShippingVo shippingVo = new ShippingVo();
            shippingVo.setReceiverAddress(shipping.getReceiverAddress());
            shippingVo.setReceiverCity(shipping.getReceiverCity());
            shippingVo.setReceiverDistrict(shipping.getReceiverDistrict());
            shippingVo.setReceiverMobile(shipping.getReceiverMobile());
            shippingVo.setReceiverName(shipping.getReceiverName());
            shippingVo.setReceiverPhone(shipping.getReceiverPhone());
            shippingVo.setReceiverProvince(shipping.getReceiverProvince());
            shippingVo.setReceiverZip(shipping.getReceiverZip());
            orderVo.setShippingVo(shippingVo);
        }

        orderVo.setPaymentTime(order.getPaymentTime());
        orderVo.setSendTime(order.getSendTime());
        orderVo.setEndTime(order.getEndTime());
        orderVo.setCloseTime(order.getCloseTime());
        orderVo.setCreateTime(order.getCreateTime());
        orderVo.setImageHost(ftpConfiguration.getServerHttpPrefix());

        // 填充订单明细
        if (!JudgeUtil.isEmpty(orderItemList)) {
            List<OrderItemVo> orderItemVos = new LinkedList<>();
            for (MmallOrderItem orderItem : orderItemList) {
                orderItemVos.add(assembleOrderItemVo(orderItem));
            }
            orderVo.setOrderItemVos(orderItemVos);
        }

        return orderVo;
    }

    private OrderItemVo assembleOrderItemVo(MmallOrderItem orderItem) {
        OrderItemVo orderItemVo = new OrderItemVo();
        orderItemVo.setCreateTime(orderItem.getCreateTime());
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setProductId(orderItem.getProductId());
        orderItemVo.setProductImage(orderItem.getProductImage());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setQuantity(orderItem.getQuantity());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());
        return orderItemVo;
    }

    private void clearCart(List<MmallCart> cartList) {
        List<Integer> ids = new LinkedList<>();
        for (MmallCart cart : cartList) {
            ids.add(cart.getId());
        }
        cartMapper.deleteBatchIds(ids);
    }

    private void reduceProductStock(List<MmallOrderItem> orderItemList) {
        for (MmallOrderItem orderItem : orderItemList) {
            MmallProduct product = productMapper.selectById(orderItem.getProductId());
            product.setStock(product.getStock() - orderItem.getQuantity());
            productMapper.updateById(product);
        }
    }

    private MmallOrder assebleOrder(Integer userId, Integer shippingId, long orderNo, BigDecimal payment) {
        MmallOrder order = new MmallOrder();
        order.setOrderNo(orderNo);
        order.setStatus(OrderSatus.NO_PAY.getCode());
        order.setPostage(0);
        order.setPaymentType(PayPlatform.ALIPAY.getCode());
        order.setPayment(payment);
        order.setShippingId(shippingId);
        order.setPaymentType(PayMentType.ONLINE_PAY.getCode());
        order.setUserId(userId);
        if (save(order)) {
            return order;
        }
        return null;
    }

    private synchronized long generatorOrderNo() {
        long currentTime = System.currentTimeMillis();
        return currentTime + new Random().nextInt(100);
    }

    private List<MmallOrderItem> getCartOrderIterms(Integer userId, Long orderNo, List<MmallCart> cartList) {
        List<MmallOrderItem> list = new LinkedList<>();
        // 校验购物车的数量，包括产品状态和数量
        for (MmallCart cart : cartList) {
            MmallProduct product = productMapper.selectById(cart.getProductId());
            if (null == product) {
                throw new WrongDataException("不存在此产品:{}", cart.getProductId());
            }

            if (!JudgeUtil.isDBEq(ProductStatus.ON_SALE.getCode(), product.getStatus())) {
                throw new WrongArgumentException("产品不是在线售卖:{}", product.getId());
            }

            // 校验库存
            if (cart.getQuantity() > product.getStock()) {
                throw new WrongArgumentException("库存不足:{}", product.getId());
            }

            MmallOrderItem orderItem = new MmallOrderItem();
            orderItem.setUserId(userId);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setOrderNo(orderNo);
            orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cart.getQuantity()));

            list.add(orderItem);
        }

        return list;
    }

}
