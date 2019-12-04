package com.zab.mmal.manager.feign;

import com.zab.mmal.api.dtos.ProductDetails;
import com.zab.mmal.api.entity.MmallProduct;
import com.zab.mmal.api.entity.MmallUser;
import com.zab.mmal.common.commons.ReturnData;
import com.zab.mmal.manager.feign.dtos.ManagerFeignFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "PROVIDERDB", path = "/provider/", fallback = ManagerFeignFallBack.class)
public interface ManagerFeignService {

    @PostMapping("user/login")
    MmallUser login(@RequestParam(value = "userName") String userName);

    @PostMapping("category/addCategory")
    ReturnData addCategory(@RequestParam(value = "categoryName") String categoryName, @RequestParam(value = "parentId") Integer parentId);

    @PostMapping("category/updateCategory")
    ReturnData updateCategory(@RequestParam(value = "categoryName") String categoryName, @RequestParam(value = "categoryId") Integer categoryId);

    @GetMapping("category/getChildrenParalleCategory/{parentId}")
    ReturnData getChildrenParalleCategory(@PathVariable(value = "parentId") Integer parentId);

    @GetMapping("category/getDeepCategory/{categoryId}")
    ReturnData getDeepCategory(@PathVariable(value = "categoryId") Integer categoryId);

    @PostMapping("product/productSave")
    ReturnData productSave(@RequestBody MmallProduct product);

    @PostMapping("product/setSaleStatus")
    ReturnData setSaleStatus(@RequestParam(value = "productId") Integer productId, @RequestParam(value = "status") Integer status);

    @GetMapping("product/getProductDetail/{productId}")
    ProductDetails getProductDetail(@PathVariable(value = "productId") Integer productId);

    @GetMapping("product/pageProduct")
    ReturnData pageProduct(@RequestParam(value = "pageNo", required = false) Integer pageNo, @RequestParam(value = "pageSize", required = false) Integer pageSize,
                           @RequestParam(value = "productName", required = false) String productName, @RequestParam(value = "productId", required = false) Integer productId);
}