package com.zab.mmal.manager.controller;

import com.zab.mmal.common.annotions.AdminOperate;
import com.zab.mmal.common.commons.ReturnData;
import com.zab.mmal.manager.feign.ManagerFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/manage/order")
@AdminOperate
public class MOrderController {

    @Autowired
    private ManagerFeignService managerFeignService;

    @GetMapping("pageOrder")
    public ReturnData pageOrder(HttpServletRequest request, @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                @RequestParam(required = false, defaultValue = "1") Integer pageNo) {
        return managerFeignService.pageOrder(null, pageSize, pageNo);
    }

    @GetMapping("getOrderDetails/{orderNo}")
    public ReturnData getOrderDetails(HttpServletRequest request, @PathVariable Long orderNo) {
        return managerFeignService.getOrderDetails(null, orderNo);
    }

    @PostMapping("sendGoods/{orderNo}")
    public ReturnData sendGoods(HttpServletRequest request, @PathVariable Long orderNo) {
        return managerFeignService.sendGoods(orderNo);
    }

}
