package com.zab.mmal.providerdb.controller;


import com.zab.mmal.api.dtos.ProductDetails;
import com.zab.mmal.api.entity.MmallProduct;
import com.zab.mmal.api.service.IMmallProductService;
import com.zab.mmal.common.commons.ReturnData;
import com.zab.mmal.common.enums.SysCodeMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zab
 * @since 2019-11-19
 */
@RestController
@RequestMapping("/provider/product")
@Slf4j
public class MmallProductController {

    @Autowired
    private IMmallProductService productService;

    @PostMapping("productSave")
    public ReturnData productSave(@RequestBody MmallProduct product) {
        boolean ok = productService.productSave(product);
        return new ReturnData(ok ? SysCodeMsg.SUCCESS : SysCodeMsg.FAIL);
    }

    @PostMapping("setSaleStatus")
    public ReturnData setSaleStatus(@RequestParam Integer productId, @RequestParam Integer status) {
        boolean ok = productService.setSaleStatus(productId, status);
        return new ReturnData(ok ? SysCodeMsg.SUCCESS : SysCodeMsg.UPDATE_FAIL);
    }

    @GetMapping("getProductDetail/{productId}")
    public ProductDetails getProductDetail(@PathVariable Integer productId) {
        return productService.getProductDetail(productId);
    }

    @GetMapping("pageProduct")
    public ReturnData pageProduct(@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageNo,
                                  @RequestParam(required = false) String productName, @RequestParam(required = false) Integer productId) {
        return new ReturnData(productService.pageProduct(pageNo, pageSize, productName, productId));
    }

    @GetMapping("pageProductByCondition")
    public ReturnData pageProductByCondition(@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageNo,
                                             @RequestParam(required = false) String keyword, @RequestParam(required = false) Integer categoryId,
                                             @RequestParam(required = false) String orderBy) {
        return new ReturnData(productService.pageProductByCondition(pageNo, pageSize, keyword, categoryId, orderBy));
    }

}
