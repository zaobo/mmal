package com.zab.mmal.protal.fegin.dtos;

import com.zab.mmal.api.dtos.ProductDetails;
import com.zab.mmal.api.entity.MmallCart;
import com.zab.mmal.api.entity.MmallUser;
import com.zab.mmal.common.commons.ReturnData;
import com.zab.mmal.common.enums.SysCodeMsg;
import com.zab.mmal.protal.fegin.ProtalFeignService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProtalFeignFallBack implements ProtalFeignService {
    @Override
    public MmallUser login(String userName) {
        return null;
    }

    @Override
    public ReturnData register(MmallUser user) {
        return new ReturnData(SysCodeMsg.FAIL);
    }

    @Override
    public String forgetGetQuestion(String userName) {
        return null;
    }

    @Override
    public String checkAnswer(String userName, String question, String answer) {
        return null;
    }

    @Override
    public ReturnData forgetRestPassword(String userName, String newPWD, String forgetToken) {
        return new ReturnData(SysCodeMsg.FAIL);
    }

    @Override
    public ReturnData restPassword(String oldPassword, String newPassword, MmallUser currentUser) {
        return new ReturnData(SysCodeMsg.MODIFY_PWD_FAIL);
    }

    @Override
    public MmallUser updateUserInfo(MmallUser user) {
        return null;
    }

    @Override
    public MmallUser getUserById(Integer id) {
        return null;
    }

    @Override
    public ProductDetails getProductDetail(Integer productId) {
        return null;
    }

    @Override
    public ReturnData pageProductByCondition(Integer pageNo, Integer pageSize, String keyword, Integer categoryId, String orderBy) {
        return new ReturnData(SysCodeMsg.FAIL);
    }

    @Override
    public ReturnData listCart(Integer userId) {
        return new ReturnData(SysCodeMsg.FAIL);
    }

    @Override
    public ReturnData addCart(MmallCart cart) {
        return new ReturnData(SysCodeMsg.FAIL);
    }

    @Override
    public ReturnData updateCart(MmallCart cart) {
        return new ReturnData(SysCodeMsg.FAIL);
    }

    @Override
    public ReturnData deleteCart(Integer userId, List<Integer> productIds) {
        return new ReturnData(SysCodeMsg.FAIL);
    }

    @Override
    public ReturnData selectOrUnSelectCart(Integer userId, Integer checked, List<Integer> productIds) {
        return new ReturnData(SysCodeMsg.FAIL);
    }

    @Override
    public ReturnData getCartProductCount(Integer userId) {
        return null;
    }

}
