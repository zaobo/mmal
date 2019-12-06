package com.zab.mmal.protal.controller;

import com.zab.mmal.api.entity.MmallShipping;
import com.zab.mmal.api.entity.MmallUser;
import com.zab.mmal.common.commons.ReturnData;
import com.zab.mmal.common.config.SessionAttribute;
import com.zab.mmal.protal.fegin.ProtalFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/service/shipping")
public class PShippingController {
    @Autowired
    private ProtalFeignService protalFeignService;

    @PostMapping("addShipping")
    public ReturnData addShipping(HttpServletRequest request, @RequestBody MmallShipping shipping) {
        MmallUser currentUser = SessionAttribute.currentUser(request.getSession());
        shipping.setUserId(currentUser.getId());
        return new ReturnData(protalFeignService.addShipping(shipping));
    }

    @PostMapping("updateShipping")
    public ReturnData updateShipping(HttpServletRequest request, @RequestBody MmallShipping shipping) {
        MmallUser currentUser = SessionAttribute.currentUser(request.getSession());
        shipping.setUserId(currentUser.getId());
        return new ReturnData(protalFeignService.updateShipping(shipping));
    }

    @PostMapping("deleteShipping/{shippingId}")
    public ReturnData deleteShipping(HttpServletRequest request, @PathVariable Integer shippingId) {
        MmallUser currentUser = SessionAttribute.currentUser(request.getSession());
        return new ReturnData(protalFeignService.deleteShipping(currentUser.getId(), shippingId));
    }

    @GetMapping("getShipping/{shippingId}")
    public ReturnData getShipping(HttpServletRequest request, @PathVariable Integer shippingId) {
        MmallUser currentUser = SessionAttribute.currentUser(request.getSession());
        return new ReturnData(protalFeignService.getShipping(currentUser.getId(), shippingId));
    }

    @PostMapping("pageShipping")
    public ReturnData pageShipping(HttpServletRequest request, @RequestParam Integer pageSize,
                                   @RequestParam Integer pageNo, @RequestBody(required = false) MmallShipping shipping) {
        MmallUser currentUser = SessionAttribute.currentUser(request.getSession());
        return new ReturnData(protalFeignService.pageShipping(currentUser.getId(), pageSize, pageNo, shipping));
    }
}
