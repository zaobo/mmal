package com.zab.mmal.providerdb.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zab.mmal.api.entity.MmallOrder;
import com.zab.mmal.api.entity.MmallUser;
import com.zab.mmal.api.service.IMmallOrderService;
import com.zab.mmal.common.commons.ReturnData;
import com.zab.mmal.common.config.SessionAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zab
 * @since 2019-11-19
 */
@RestController
@RequestMapping("/provider/order")
public class MmallOrderController {

    @Autowired
    private IMmallOrderService orderService;

    @GetMapping("getOrder/{userId}/{orderNo}")
    public MmallOrder getOrder(@PathVariable Integer userId, @PathVariable Long orderNo) {
        QueryWrapper<MmallOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("order_no", orderNo);
        return orderService.getOne(queryWrapper);
    }

    @GetMapping("getOrderByNo/{orderNo}")
    public MmallOrder getOrderByNo(@PathVariable(value = "orderNo") Long orderNo) {
        QueryWrapper<MmallOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        return orderService.getOne(queryWrapper);
    }

    @PostMapping("updateOrderStatus")
    public ReturnData updateOrderStatus(@RequestBody MmallOrder order) {
        UpdateWrapper<MmallOrder> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("order_no", order.getOrderNo());
        return new ReturnData(orderService.update(order, updateWrapper));
    }

    @PostMapping("creatOrder/{userId}/{shippingId}")
    public ReturnData creatOrder(@PathVariable Integer userId, @PathVariable Integer shippingId) {
        return new ReturnData(orderService.createOrder(userId, shippingId));
    }

    @PostMapping("cancelOrder/{userId}/{orderNo}")
    public ReturnData cancelOrder(@PathVariable Integer userId, @PathVariable Long orderNo) {
        return new ReturnData(orderService.cancelOrder(userId, orderNo));
    }

    @GetMapping("getOrderCartProduct/{userId}")
    public ReturnData getOrderCartProduct(@PathVariable Integer userId) {
        return new ReturnData(orderService.getOrderCartProduct(userId));
    }

    @GetMapping("getOrderDetails/{orderNo}")
    public ReturnData getOrderDetails(@RequestParam(required = false) Integer userId, @PathVariable Long orderNo) {
        return new ReturnData(orderService.getOrderDetails(userId, orderNo));
    }

    @GetMapping("pageOrder")
    public ReturnData pageOrder(@RequestParam(required = false) Integer userId, @RequestParam Integer pageSize,
                                @RequestParam Integer pageNo) {
        return new ReturnData(orderService.pageOrder(userId, pageSize, pageNo));
    }

    @PostMapping("sendGoods/{orderNo}")
    public ReturnData sendGoods(@PathVariable Long orderNo) {
        return new ReturnData(orderService.sendGoods(orderNo));
    }

}
