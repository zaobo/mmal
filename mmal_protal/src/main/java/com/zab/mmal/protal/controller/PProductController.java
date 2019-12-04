package com.zab.mmal.protal.controller;

import com.zab.mmal.api.dtos.ProductDetails;
import com.zab.mmal.common.commons.ReturnData;
import com.zab.mmal.common.enums.ProductStatus;
import com.zab.mmal.common.enums.SysCodeMsg;
import com.zab.mmal.common.utils.JudgeUtil;
import com.zab.mmal.protal.fegin.ProtalFeignService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/service/product")
public class PProductController {

    @Autowired
    private ProtalFeignService protalFeignService;

    @GetMapping("getProductDetail/{productId}")
    public ReturnData getProductDetail(HttpServletRequest request, @PathVariable(value = "productId") Integer productId) {
        ProductDetails productDetails = protalFeignService.getProductDetail(productId);
        if (productDetails == null) {
            return new ReturnData(SysCodeMsg.FAIL.getCode(), "商品不存在");
        }
        if (!JudgeUtil.isDBEq(productDetails.getStatus(), ProductStatus.ON_SALE.getCode())) {
            return new ReturnData(SysCodeMsg.FAIL.getCode(), "商品已下架或者删除");
        }
        return new ReturnData(productDetails);
    }

    @GetMapping("pageProductByCondition")
    public ReturnData pageProductByCondition(HttpServletRequest request, @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                             @RequestParam(required = false, defaultValue = "1") Integer pageNo, @RequestParam(required = false) String keyword,
                                             @RequestParam(required = false) Integer categoryId, @RequestParam(required = false) String orderBy) {
        if (StringUtils.isBlank(keyword) && null == categoryId) {
            return new ReturnData(SysCodeMsg.PARAM_IS_NULL.getCode(), "关键字和分类ID不能同时为空");
        }
        return protalFeignService.pageProductByCondition(pageNo, pageSize, keyword, categoryId, orderBy);
    }

}
