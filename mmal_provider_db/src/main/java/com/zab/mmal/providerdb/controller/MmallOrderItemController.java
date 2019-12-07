package com.zab.mmal.providerdb.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zab.mmal.api.entity.MmallOrderItem;
import com.zab.mmal.api.service.IMmallOrderItemService;
import com.zab.mmal.common.commons.ReturnData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zab
 * @since 2019-11-19
 */
@RestController
@RequestMapping("/provider/orderitem")
public class MmallOrderItemController {

    @Autowired
    private IMmallOrderItemService orderItemService;

    @GetMapping("getOrderItemList/{userId}/{orderNo}")
    public ReturnData getOrderItemList(@PathVariable Integer userId, @PathVariable Long orderNo) {
        QueryWrapper<MmallOrderItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("order_no", orderNo);
        return new ReturnData(orderItemService.list(queryWrapper));
    }

}
