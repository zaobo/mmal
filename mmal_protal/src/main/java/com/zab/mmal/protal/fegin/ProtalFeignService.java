package com.zab.mmal.protal.fegin;

import com.zab.mmal.api.dtos.ProductDetails;
import com.zab.mmal.api.entity.*;
import com.zab.mmal.common.commons.ReturnData;
import com.zab.mmal.protal.fegin.dtos.ProtalFeignFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "PROVIDERDB", path = "/provider/", fallback = ProtalFeignFallBack.class)
public interface ProtalFeignService {

    @PostMapping(value = "user/login")
    MmallUser login(@RequestParam(value = "userName") String userName);

    @PostMapping("user/register")
    ReturnData register(@RequestBody MmallUser user);

    @PostMapping("user/forgetGetQuestion")
    String forgetGetQuestion(@RequestParam(value = "userName") String userName);

    @PostMapping("user/checkAnswer")
    String checkAnswer(@RequestParam(value = "userName") String userName, @RequestParam(value = "question") String question, @RequestParam(value = "answer") String answer);

    @PostMapping("user/forgetRestPassword")
    ReturnData forgetRestPassword(@RequestParam(value = "userName") String userName, @RequestParam(value = "newPWD") String newPWD, @RequestParam(value = "forgetToken") String forgetToken);

    @PostMapping("user/restPassword")
    ReturnData restPassword(@RequestParam(value = "oldPassword") String oldPassword, @RequestParam(value = "newPassword") String newPassword, @RequestBody MmallUser currentUser);

    @PostMapping("user/updateUserInfo")
    MmallUser updateUserInfo(@RequestBody MmallUser user);

    @PostMapping("user/getUserById")
    MmallUser getUserById(@RequestParam(value = "id") Integer id);

    @GetMapping("product/getProductDetail/{productId}")
    ProductDetails getProductDetail(@PathVariable(value = "productId") Integer productId);

    @GetMapping("product/pageProductByCondition")
    ReturnData pageProductByCondition(@RequestParam(value = "pageNo", required = false) Integer pageNo, @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                      @RequestParam(value = "keyword", required = false) String keyword, @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                      @RequestParam(value = "orderBy", required = false) String orderBy);

    @GetMapping("cart/listCart/{userId}")
    ReturnData listCart(@PathVariable(value = "userId") Integer userId);

    @PostMapping("cart/addCart")
    ReturnData addCart(@RequestBody MmallCart cart);

    @PostMapping("cart/updateCart")
    ReturnData updateCart(@RequestBody MmallCart cart);

    @PostMapping("cart/deleteCart/{userId}")
    ReturnData deleteCart(@PathVariable(value = "userId") Integer userId, @RequestBody List<Integer> productIds);

    /**
     * 全选
     * 全不选
     * 批量选
     * 批量反选
     */
    @PostMapping("cart/selectOrUnSelectCart/{userId}/{checked}")
    ReturnData selectOrUnSelectCart(@PathVariable(value = "userId") Integer userId, @PathVariable(value = "checked") Integer checked,
                                    @RequestBody(required = false) List<Integer> productIds);

    /**
     * 查询当前用户的购物车里面的产品数量，如果一个产品有10个，那么数量就是10
     */
    @GetMapping("cart/getCartProductCount/{userId}")
    ReturnData getCartProductCount(@PathVariable(value = "userId") Integer userId);

    @PostMapping("shipping/addShipping")
    ReturnData addShipping(@RequestBody MmallShipping shipping);

    @PostMapping("shipping/updateShipping")
    ReturnData updateShipping(@RequestBody MmallShipping shipping);

    @PostMapping("shipping/deleteShipping/{userId}/{shippingId}")
    ReturnData deleteShipping(@PathVariable(value = "userId") Integer userId, @PathVariable(value = "shippingId") Integer shippingId);

    @GetMapping("shipping/getShipping/{userId}/{shippingId}")
    ReturnData getShipping(@PathVariable(value = "userId") Integer userId, @PathVariable(value = "shippingId") Integer shippingId);

    @PostMapping("shipping/pageShipping/{userId}")
    ReturnData pageShipping(@PathVariable(value = "userId") Integer userId, @RequestParam(value = "pageSize") Integer pageSize,
                            @RequestParam(value = "pageNo") Integer pageNo, @RequestBody(required = false) MmallShipping shipping);

    @GetMapping("order/getOrder/{userId}/{orderNo}/{path}")
    ReturnData getOrder(@PathVariable(value = "userId") Integer userId, @PathVariable(value = "orderNo") Long orderNo);

    @GetMapping("orderitem/getOrderItemList/{userId}/{orderNo}")
    ReturnData getOrderItemList(@PathVariable(value = "userId") Integer userId, @PathVariable(value = "orderNo") Long orderNo);

    @GetMapping("order/getOrderByNo/{orderNo}")
    ReturnData getOrderByNo(@PathVariable(value = "orderNo") Long orderNo);

    @PostMapping("order/updateOrderStatus")
    ReturnData updateOrderStatus(@RequestBody MmallOrder order);

    @PostMapping("payinfo/addPayInfo")
    ReturnData addPayInfo(@RequestBody MmallPayInfo payInfo);

}
