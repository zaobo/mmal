package com.zab.mmal.api.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zab.mmal.api.dtos.ProductDetails;
import com.zab.mmal.api.dtos.ProductList;
import com.zab.mmal.api.entity.MmallProduct;

import java.io.File;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zab
 * @since 2019-11-19
 */
public interface IMmallProductService extends IService<MmallProduct> {

    boolean productSave(MmallProduct product);

    boolean setSaleStatus(Integer productId, Integer status);

    ProductDetails getProductDetail(Integer productId);

    Page<ProductList> pageProduct(Integer pageNo, Integer pageSize, String productName, Integer productId);

    Page<ProductList> pageProductByCondition(Integer pageNo, Integer pageSize, String keyword, Integer categoryId, String orderBy);
}
