package com.zab.mmal.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zab.mmal.api.dao.ICategoryDao;
import com.zab.mmal.api.dtos.ProductDetails;
import com.zab.mmal.api.dtos.ProductList;
import com.zab.mmal.api.entity.MmallCategory;
import com.zab.mmal.api.entity.MmallProduct;
import com.zab.mmal.api.service.IMmallProductService;
import com.zab.mmal.common.config.Constant;
import com.zab.mmal.common.utils.JudgeUtil;
import com.zab.mmal.core.config.FtpConfiguration;
import com.zab.mmal.core.mapper.MmallCategoryMapper;
import com.zab.mmal.core.mapper.MmallProductMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zab
 * @since 2019-11-19
 */
@Service
public class MmallProductServiceImpl extends ServiceImpl<MmallProductMapper, MmallProduct> implements IMmallProductService {

    @Resource
    private FtpConfiguration ftpConfiguration;
    @Resource
    private MmallCategoryMapper categoryMapper;
    @Resource
    private ICategoryDao categoryDao;

    @Override
    @Transactional
    public boolean productSave(MmallProduct product) {
        if (StringUtils.isNotBlank(product.getSubImages())) {
            String[] subImages = product.getSubImages().split(",");
            if (!JudgeUtil.isEmpty(subImages)) {
                product.setMainImage(subImages[0]);
            }
        }

        boolean ok;
        if (JudgeUtil.isDBNull(product.getId())) {
            ok = save(product);
        } else {
            product.setUpdateTime(new Date());
            ok = updateById(product);
        }
        return ok;
    }

    @Override
    @Transactional
    public boolean setSaleStatus(Integer productId, Integer status) {
        MmallProduct product = new MmallProduct();
        product.setId(productId);
        product.setStatus(status);
        return updateById(product);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public ProductDetails getProductDetail(Integer productId) {
        MmallProduct product = getById(productId);
        if (null == product) {
            return null;
        }
        ProductDetails details = new ProductDetails();
        BeanUtils.copyProperties(product, details);
        details.setImagesHost(ftpConfiguration.getServerHttpPrefix());
        MmallCategory category = categoryMapper.selectById(details.getCategoryId());
        Integer parentCategoryId;
        if (null == category) {
            parentCategoryId = 0;
        } else {
            parentCategoryId = category.getParentId();
        }
        details.setParentCategoryId(parentCategoryId);
        return details;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public Page<ProductList> pageProduct(Integer pageNo, Integer pageSize, String productName, Integer productId) {
        Page<MmallProduct> productPage = new Page(pageNo, pageSize);
        QueryWrapper<MmallProduct> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(productName)) {
            queryWrapper.like("name", productName);
        }
        if (null != productId) {
            queryWrapper.eq("id", productId);
        }
        Page<MmallProduct> mmallProductPage = (Page<MmallProduct>) page(productPage, queryWrapper);
        List<ProductList> list = new LinkedList<>();
        if (!JudgeUtil.isEmpty(mmallProductPage.getRecords())) {
            mmallProductPage.getRecords().forEach(mmallProduct -> {
                list.add(this.assembleProductList(mmallProduct));
            });
        }

        Page<ProductList> listPage = new Page<>(pageNo, pageSize);
        listPage.setRecords(list);
        listPage.setTotal(mmallProductPage.getTotal());
        listPage.setSize(mmallProductPage.getSize());
        return listPage;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public Page<ProductList> pageProductByCondition(Integer pageNo, Integer pageSize, String keyword, Integer categoryId, String orderBy) {
        Page<ProductList> page = new Page<>(pageNo, pageSize);

        List<Integer> categoryList = new LinkedList<>();
        if (null != categoryId) {
            MmallCategory mmallCategory = categoryMapper.selectById(categoryId);
            if (null == mmallCategory && StringUtils.isBlank(keyword)) {
                // 没有该分类，并且还没有关键字，这个时候返回一个null
                page.setRecords(null);
            }

            categoryList = categoryDao.getDeepCategory(categoryId);
        }


        QueryWrapper<MmallProduct> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.like("name", keyword);
        }
        if (categoryList.size() > 0) {
            queryWrapper.in("category_id", categoryList);
        }

        // 排序处理
        if (StringUtils.isNotBlank(orderBy)) {
            if (Constant.PRICE_ASC_DESC.contains(orderBy)) {
                String[] arr = orderBy.split("_");
                if (StringUtils.equalsIgnoreCase("asc", arr[1])) {
                    queryWrapper.orderByAsc(arr[0]);
                } else {
                    queryWrapper.orderByDesc(arr[0]);
                }
            }
        }

        // 分页查询产品
        Page<MmallProduct> productPage = new Page(pageNo, pageSize);
        Page<MmallProduct> mmallProductPage = (Page<MmallProduct>) page(productPage, queryWrapper);

        List<ProductList> list = new LinkedList<>();
        if (!JudgeUtil.isEmpty(mmallProductPage.getRecords())) {
            mmallProductPage.getRecords().forEach(mmallProduct -> {
                list.add(this.assembleProductList(mmallProduct));
            });
        }
        page.setRecords(list);
        page.setTotal(mmallProductPage.getTotal());
        page.setSize(mmallProductPage.getSize());
        return page;
    }

    private ProductList assembleProductList(MmallProduct product) {
        ProductList productList = new ProductList();
        productList.setCategoryId(product.getCategoryId());
        productList.setId(product.getId());
        productList.setImagesHost(ftpConfiguration.getServerHttpPrefix());
        productList.setMainImage(product.getMainImage());
        productList.setName(product.getName());
        productList.setPrice(product.getPrice());
        productList.setStatus(product.getStatus());
        productList.setSubtitle(product.getSubtitle());
        return productList;
    }

}
