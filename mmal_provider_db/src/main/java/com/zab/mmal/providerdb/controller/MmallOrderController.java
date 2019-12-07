package com.zab.mmal.providerdb.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

    @GetMapping("pay/{userId}/{orderNo}/{path}")
    public ReturnData pay(@PathVariable Integer userId, @PathVariable Long orderNo) {
        QueryWrapper<MmallOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("order_no", orderNo);
        MmallOrder order = orderService.getOne(queryWrapper);
        return new ReturnData(order);
    }

}
