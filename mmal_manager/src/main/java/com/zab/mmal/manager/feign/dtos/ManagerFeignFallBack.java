package com.zab.mmal.manager.feign.dtos;

import com.zab.mmal.api.dtos.ProductDetails;
import com.zab.mmal.api.entity.MmallProduct;
import com.zab.mmal.api.entity.MmallUser;
import com.zab.mmal.common.commons.ReturnData;
import com.zab.mmal.common.enums.SysCodeMsg;
import com.zab.mmal.manager.feign.ManagerFeignService;
import org.springframework.stereotype.Component;

@Component
public class ManagerFeignFallBack implements ManagerFeignService {
    @Override
    public MmallUser login(String userName) {
        return null;
    }

    @Override
    public ReturnData addCategory(String categoryName, Integer parentId) {
        return new ReturnData(SysCodeMsg.FAIL);
    }

    @Override
    public ReturnData updateCategory(String categoryName, Integer categoryId) {
        return new ReturnData(SysCodeMsg.UPDATE_FAIL);
    }

    @Override
    public ReturnData getChildrenParalleCategory(Integer parentId) {
        return new ReturnData(SysCodeMsg.FAIL);
    }

    @Override
    public ReturnData getDeepCategory(Integer categoryId) {
        return new ReturnData(SysCodeMsg.FAIL);
    }

    @Override
    public ReturnData productSave(MmallProduct product) {
        return new ReturnData(SysCodeMsg.FAIL);
    }

    @Override
    public ReturnData setSaleStatus(Integer productId, Integer status) {
        return new ReturnData(SysCodeMsg.UPDATE_FAIL);
    }

    @Override
    public ProductDetails getProductDetail(Integer productId) {
        return null;
    }

    @Override
    public ReturnData pageProduct(Integer pageNo, Integer pageSize, String productName, Integer productId) {
        return new ReturnData(SysCodeMsg.FAIL);
    }

    @Override
    public ReturnData getOrderDetails(Integer userId, Long orderNo) {
        return new ReturnData(SysCodeMsg.FAIL);
    }

    @Override
    public ReturnData pageOrder(Integer userId, Integer pageSize, Integer pageNo) {
        return new ReturnData(SysCodeMsg.FAIL);
    }

    @Override
    public ReturnData sendGoods(Long orderNo) {
        return new ReturnData(SysCodeMsg.FAIL);
    }

}
