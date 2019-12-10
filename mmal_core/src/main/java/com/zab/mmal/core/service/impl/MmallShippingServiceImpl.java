package com.zab.mmal.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zab.mmal.api.entity.MmallShipping;
import com.zab.mmal.api.service.IMmallShippingService;
import com.zab.mmal.common.utils.JudgeUtil;
import com.zab.mmal.common.utils.ObjectIsEmptytils;
import com.zab.mmal.common.utils.StringEasyUtil;
import com.zab.mmal.core.mapper.MmallShippingMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zab
 * @since 2019-11-19
 */
@Service
public class MmallShippingServiceImpl extends ServiceImpl<MmallShippingMapper, MmallShipping> implements IMmallShippingService {

    @Override
    @Transactional
    public Integer addShipping(MmallShipping shipping) {
        if (save(shipping)) {
            return shipping.getId();
        }
        return null;
    }

    @Override
    @Transactional
    public boolean deleteShipping(Integer userId, Integer shippingId) {
        QueryWrapper<MmallShipping> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("id", shippingId);
        return remove(queryWrapper);
    }

    @Override
    @Transactional
    public boolean updateShipping(MmallShipping shipping) {
        UpdateWrapper<MmallShipping> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", shipping.getUserId());
        updateWrapper.eq("id", shipping.getId());
        shipping.setId(null);
        shipping.setUpdateTime(new Date());
        shipping.setUserId(null);
        return update(shipping, updateWrapper);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public MmallShipping getShipping(Integer userId, Integer shippingId) {
        QueryWrapper<MmallShipping> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("id", shippingId);
        return getOne(queryWrapper);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public Page<MmallShipping> pageShipping(Integer userId, Integer pageSize, Integer pageNo, MmallShipping shipping) {
        QueryWrapper<MmallShipping> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        // 是否有额外查询条件
        Map<String, Object> conditionMap = ObjectIsEmptytils.getNotEmptyField(shipping);
        if (!JudgeUtil.isEmpty(conditionMap)) {
            for (String key : conditionMap.keySet()) {
                String queryKey = StringEasyUtil.camelToUnderline(key);
                if (queryKey.toLowerCase().matches(".*name.*")) {
                    queryWrapper.like(queryKey, conditionMap.get(key));
                } else {
                    queryWrapper.eq(queryKey, conditionMap.get(key));
                }
            }
        }

        return (Page<MmallShipping>) page(new Page<MmallShipping>(pageNo, pageSize), queryWrapper);
    }
}
