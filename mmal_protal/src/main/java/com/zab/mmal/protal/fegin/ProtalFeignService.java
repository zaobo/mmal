package com.zab.mmal.protal.fegin;

import com.zab.mmal.api.dtos.ProductDetails;
import com.zab.mmal.api.entity.MmallUser;
import com.zab.mmal.common.commons.ReturnData;
import com.zab.mmal.protal.fegin.dtos.ProtalFeignFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

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
}
