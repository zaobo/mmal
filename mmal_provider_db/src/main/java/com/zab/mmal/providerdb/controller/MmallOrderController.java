package com.zab.mmal.providerdb.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zab.mmal.api.entity.MmallOrder;
import com.zab.mmal.api.service.IMmallOrderService;
import com.zab.mmal.common.commons.ReturnData;
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

}
