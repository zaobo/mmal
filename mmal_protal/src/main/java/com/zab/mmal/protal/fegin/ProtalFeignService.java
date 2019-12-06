package com.zab.mmal.protal.fegin;

import com.zab.mmal.api.dtos.ProductDetails;
import com.zab.mmal.api.entity.MmallCart;
import com.zab.mmal.api.entity.MmallShipping;
import com.zab.mmal.api.entity.MmallUser;
import com.zab.mmal.common.commons.ReturnData;
import com.zab.mmal.common.enums.SysCodeMsg;
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

    @GetMapping("listCart/{userId}")
    ReturnData listCart(@PathVariable(value = "userId") Integer userId);

    @PostMapping("addCart")
    ReturnData addCart(@RequestBody MmallCart cart);

    @PostMapping("updateCart")
    ReturnData updateCart(@RequestBody MmallCart cart);

    @PostMapping("deleteCart/{userId}")
    ReturnData deleteCart(@PathVariable(value = "userId") Integer userId, @RequestBody List<Integer> productIds);

    /**
     * 全选
     * 全不选
     * 批量选
     * 批量反选
     */
    @PostMapping("selectOrUnSelectCart/{userId}/{checked}")
    ReturnData selectOrUnSelectCart(@PathVariable(value = "userId") Integer userId, @PathVariable(value = "checked") Integer checked,
                                    @RequestBody(required = false) List<Integer> productIds);

    /**
     * 查询当前用户的购物车里面的产品数量，如果一个产品有10个，那么数量就是10
     */
    @GetMapping("getCartProductCount/{userId}")
    ReturnData getCartProductCount(@PathVariable(value = "userId") Integer userId);

    @PostMapping("addShipping")
    public ReturnData addShipping(@RequestBody MmallShipping shipping);

    @PostMapping("updateShipping")
    public ReturnData updateShipping(@RequestBody MmallShipping shipping);

    @PostMapping("deleteShipping/{userId}/{shippingId}")
    public ReturnData deleteShipping(@PathVariable(value = "userId") Integer userId, @PathVariable(value = "shippingId") Integer shippingId);

    @GetMapping("getShipping/{userId}/{shippingId}")
    public ReturnData getShipping(@PathVariable(value = "userId") Integer userId, @PathVariable(value = "shippingId") Integer shippingId);

    @PostMapping("pageShipping/{userId}")
    public ReturnData pageShipping(@PathVariable(value = "userId") Integer userId, @RequestParam(value = "pageSize") Integer pageSize,
                                   @RequestParam(value = "pageNo") Integer pageNo, @RequestBody(required = false) MmallShipping shipping);
}
