package com.zab.mmal.providerdb.controller;


import com.zab.mmal.api.entity.MmallShipping;
import com.zab.mmal.api.service.IMmallShippingService;
import com.zab.mmal.common.commons.ReturnData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zab
 * @since 2019-11-19
 */
@RestController
@RequestMapping("/provider/shipping")
public class MmallShippingController {

    @Autowired
    private IMmallShippingService shippingService;

    @PostMapping("addShipping")
    public ReturnData addShipping(@RequestBody MmallShipping shipping) {
        return new ReturnData(shippingService.addShipping(shipping));
    }

    @PostMapping("updateShipping")
    public ReturnData updateShipping(@RequestBody MmallShipping shipping) {
        return new ReturnData(shippingService.updateShipping(shipping));
    }

    @PostMapping("deleteShipping/{userId}/{shippingId}")
    public ReturnData deleteShipping(@PathVariable Integer userId, @PathVariable Integer shippingId) {
        return new ReturnData(shippingService.deleteShipping(userId, shippingId));
    }

    @GetMapping("getShipping/{userId}/{shippingId}")
    public ReturnData getShipping(@PathVariable Integer userId, @PathVariable Integer shippingId) {
        return new ReturnData(shippingService.getShipping(userId, shippingId));
    }

    @PostMapping("pageShipping/{userId}")
    public ReturnData pageShipping(@PathVariable Integer userId, @RequestParam Integer pageSize,
                                   @RequestParam Integer pageNo, @RequestBody(required = false) MmallShipping shipping) {
        return new ReturnData(shippingService.pageShipping(userId, pageSize, pageNo, shipping));
    }

}
