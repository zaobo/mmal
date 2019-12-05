package com.zab.mmal.protal.controller;

import com.zab.mmal.api.entity.MmallCart;
import com.zab.mmal.api.entity.MmallUser;
import com.zab.mmal.common.commons.ReturnData;
import com.zab.mmal.common.config.SessionAttribute;
import com.zab.mmal.common.enums.SysCodeMsg;
import com.zab.mmal.common.utils.JudgeUtil;
import com.zab.mmal.protal.fegin.ProtalFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/service/cart/")
public class PCartController {

    @Autowired
    private ProtalFeignService protalFeignService;

    @GetMapping("listCart")
    public ReturnData listCart(HttpServletRequest request) {
        MmallUser currentUser = SessionAttribute.currentUser(request.getSession());
        return new ReturnData(protalFeignService.listCart(currentUser.getId()));
    }

    @PostMapping("addCart")
    public ReturnData addCart(HttpServletRequest request, @RequestBody MmallCart cart) {
        if (null == cart.getQuantity() || null == cart.getProductId()) {
            return new ReturnData(SysCodeMsg.PARAM_IS_NULL.getCode(), "产品id必填，数量必填");
        }

        MmallUser currentUser = SessionAttribute.currentUser(request.getSession());
        cart.setUserId(currentUser.getId());
        return new ReturnData(protalFeignService.addCart(cart));
    }

    @PostMapping("updateCart")
    public ReturnData updateCart(HttpServletRequest request, @RequestBody MmallCart cart) {
        if (null == cart.getQuantity() || null == cart.getProductId()) {
            return new ReturnData(SysCodeMsg.PARAM_IS_NULL.getCode(), "产品id必填，数量必填");
        }

        MmallUser currentUser = SessionAttribute.currentUser(request.getSession());
        cart.setUserId(currentUser.getId());
        return new ReturnData(protalFeignService.updateCart(cart));
    }

    @PostMapping("deleteCart")
    public ReturnData deleteCart(HttpServletRequest request, @RequestBody List<Integer> productIds) {
        if(JudgeUtil.isEmpty(productIds)){
            return new ReturnData(SysCodeMsg.PARAM_IS_NULL.getCode(),"请选择要删除的产品");
        }
        MmallUser currentUser = SessionAttribute.currentUser(request.getSession());
        return new ReturnData(protalFeignService.deleteCart(currentUser.getId(), productIds));
    }

    /**
     * 全选
     * 全不选
     * 批量选
     * 批量反选
     */
    @PostMapping("selectOrUnSelectCart/{checked}")
    public ReturnData selectOrUnSelectAllCart(HttpServletRequest request, @PathVariable Integer checked,
                                              @RequestBody(required = false) List<Integer> productIds) {
        MmallUser currentUser = SessionAttribute.currentUser(request.getSession());
        return new ReturnData(protalFeignService.selectOrUnSelectCart(currentUser.getId(), checked, productIds));
    }

    /**
     * 查询当前用户的购物车里面的产品数量，如果一个产品有10个，那么数量就是10
     */
    @GetMapping("getCartProductCount")
    public ReturnData getCartProductCount(HttpServletRequest request) {
        MmallUser currentUser = SessionAttribute.currentUser(request.getSession());
        return new ReturnData(protalFeignService.getCartProductCount(currentUser.getId()));
    }

}
