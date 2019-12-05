package com.zab.mmal.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zab.mmal.api.dtos.CartProductVo;
import com.zab.mmal.api.dtos.CartVo;
import com.zab.mmal.api.entity.MmallCart;
import com.zab.mmal.api.entity.MmallProduct;
import com.zab.mmal.api.service.IMmallCartService;
import com.zab.mmal.common.config.FtpConfiguration;
import com.zab.mmal.common.enums.CheckStatus;
import com.zab.mmal.common.enums.StockLimit;
import com.zab.mmal.common.utils.BigDecimalUtil;
import com.zab.mmal.common.utils.JudgeUtil;
import com.zab.mmal.core.mapper.MmallCartMapper;
import com.zab.mmal.core.mapper.MmallProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
public class MmallCartServiceImpl extends ServiceImpl<MmallCartMapper, MmallCart> implements IMmallCartService {

    @Resource
    private MmallProductMapper productMapper;
    @Resource
    private MmallCartMapper cartMapper;
    @Resource
    private FtpConfiguration ftpConfiguration;

    @Transactional
    @Override
    public CartVo addCatr(MmallCart cart) {
        QueryWrapper<MmallCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", cart.getUserId());
        queryWrapper.eq("product_id", cart.getProductId());
        MmallCart oldCart = getOne(queryWrapper);

        boolean ok;
        if (null == oldCart) {
            // 如果这个产品不再购物车里面，则新增一个
            cart.setChecked(CheckStatus.CHECKED.getCode());
            ok = save(cart);
        } else {
            int count = oldCart.getQuantity() + cart.getQuantity();
            oldCart.setQuantity(count);
            oldCart.setUpdateTime(new Date());
            updateById(oldCart);
        }
        return this.getCartVoLimit(cart.getUserId());
    }

    @Transactional
    @Override
    public CartVo updateCart(MmallCart cart) {
        QueryWrapper<MmallCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", cart.getUserId());
        queryWrapper.eq("product_id", cart.getProductId());
        MmallCart oldCart = getOne(queryWrapper);
        if (null != oldCart) {
            MmallCart updateCart = new MmallCart();
            updateCart.setId(oldCart.getId());
            updateCart.setUpdateTime(new Date());
            updateCart.setQuantity(cart.getQuantity());
            updateById(updateCart);
        }
        return this.getCartVoLimit(cart.getUserId());
    }

    @Override
    @Transactional
    public CartVo deleteCart(Integer userId, List<Integer> productIds) {
        QueryWrapper<MmallCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.in("product_id", productIds);
        remove(queryWrapper);
        return this.getCartVoLimit(userId);
    }

    @Override
    @Transactional
    public CartVo listCart(Integer userId) {
        return this.getCartVoLimit(userId);
    }

    @Override
    @Transactional
    public CartVo selectOrUnSelectCart(Integer userId, Integer checked, List<Integer> productIds) {
        UpdateWrapper<MmallCart> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId);
        if (!JudgeUtil.isEmpty(productIds)) {
            updateWrapper.in("product_id", productIds);
        }

        MmallCart cart = new MmallCart();
        cart.setChecked(checked);
        cart.setUpdateTime(new Date());
        update(cart, updateWrapper);
        return this.getCartVoLimit(userId);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public Integer getCartProductCount(Integer userId) {
        return cartMapper.getCartProductCount(userId);
    }

    private CartVo getCartVoLimit(Integer userId) {
        CartVo cartVo = new CartVo();

        QueryWrapper<MmallCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<MmallCart> carts = list(queryWrapper);
        // 购物车全部总价
        BigDecimal cartTotalPrice = new BigDecimal("0");
        List<CartProductVo> cartProductVoList = new LinkedList<>();
        if (!JudgeUtil.isEmpty(carts)) {
            for (MmallCart cart : carts) {
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cart.getId());
                cartProductVo.setProductId(cart.getProductId());
                cartProductVo.setUserId(cart.getUserId());

                //查询产品信息
                MmallProduct product = productMapper.selectById(cart.getProductId());
                if (null != product) {
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductStock(product.getStock());
                    cartProductVo.setProductSubtitle(product.getSubtitle());

                    // 判断库存
                    int buyLimitCount;
                    if (product.getStock() >= cart.getQuantity()) {
                        buyLimitCount = cart.getQuantity();
                        cartProductVo.setLimitQuantity(StockLimit.LIMIT_NUM_SUCCESS.name());
                    } else {
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(StockLimit.LIMIT_NUM_FAIL.name());

                        // 购物车数量大于库存则更新购物车的数量为最大库存数
                        MmallCart cartForQuantity = new MmallCart();
                        cartForQuantity.setId(cart.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartForQuantity.setUpdateTime(new Date());
                        updateById(cartForQuantity);
                    }

                    cartProductVo.setQuantity(buyLimitCount);
                    //计算单个购物车总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), buyLimitCount));
                    cartProductVo.setProductChecked(cart.getChecked());
                }

                if (JudgeUtil.isDBEq(cartProductVo.getProductChecked(), CheckStatus.CHECKED.getCode())) {
                    // 如果已经勾选，增加到总价中
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));
        cartVo.setImageHost(ftpConfiguration.getServerHttpPrefix());
        return cartVo;
    }

    private boolean getAllCheckedStatus(Integer userId) {
        if (null == userId) {
            return false;
        }
        QueryWrapper<MmallCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("checked", CheckStatus.UN_CHECKED.getCode());
        return JudgeUtil.isDBEq(count(queryWrapper), 0);
    }

}
