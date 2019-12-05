package com.zab.mmal.providerdb.controller;


import com.zab.mmal.api.entity.MmallCart;
import com.zab.mmal.api.service.IMmallCartService;
import com.zab.mmal.common.commons.ReturnData;
import com.zab.mmal.common.enums.SysCodeMsg;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zab
 * @since 2019-11-19
 */
@RestController
@RequestMapping("/provider/cart")
public class MmallCartController {

    private IMmallCartService mmallCartService;

    @GetMapping("listCart/{userId}")
    public ReturnData listCart(@PathVariable Integer userId) {
        if (null == userId) {
            return new ReturnData(SysCodeMsg.PARAM_IS_NULL.getCode(), "用户id必填");
        }
        return new ReturnData(mmallCartService.listCart(userId));
    }

    @PostMapping("addCart")
    public ReturnData addCart(@RequestBody MmallCart cart) {
        if (null == cart.getUserId()) {
            return new ReturnData(SysCodeMsg.PARAM_IS_NULL.getCode(), "用户id必填");
        }
        return new ReturnData(mmallCartService.addCatr(cart));
    }

    @PostMapping("updateCart")
    public ReturnData updateCart(@RequestBody MmallCart cart) {
        if (null == cart.getUserId()) {
            return new ReturnData(SysCodeMsg.PARAM_IS_NULL.getCode(), "用户id必填");
        }
        return new ReturnData(mmallCartService.updateCart(cart));
    }

    @PostMapping("deleteCart/{userId}")
    public ReturnData deleteCart(@PathVariable Integer userId, @RequestBody List<Integer> productIds) {
        if (null == userId) {
            return new ReturnData(SysCodeMsg.PARAM_IS_NULL.getCode(), "用户id必填");
        }
        return new ReturnData(mmallCartService.deleteCart(userId, productIds));
    }

    /**
     * 全选
     * 全不选
     * 批量选
     * 批量反选
     */
    @PostMapping("selectOrUnSelectCart/{userId}/{checked}")
    public ReturnData selectOrUnSelectCart(@PathVariable Integer userId, @PathVariable Integer checked,
                                           @RequestBody(required = false) List<Integer> productIds) {
        if (null == userId) {
            return new ReturnData(SysCodeMsg.PARAM_IS_NULL.getCode(), "用户id必填");
        }
        return new ReturnData(mmallCartService.selectOrUnSelectCart(userId, checked, productIds));
    }

    /**
     * 查询当前用户的购物车里面的产品数量，如果一个产品有10个，那么数量就是10
     */
    @GetMapping("getCartProductCount/{userId}")
    public ReturnData getCartProductCount(@PathVariable Integer userId) {
        return new ReturnData(mmallCartService.getCartProductCount(userId));
    }

}
