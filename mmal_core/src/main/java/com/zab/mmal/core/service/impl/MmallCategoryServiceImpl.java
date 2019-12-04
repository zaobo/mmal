package com.zab.mmal.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.zab.mmal.api.dao.ICategoryDao;
import com.zab.mmal.api.entity.MmallCategory;
import com.zab.mmal.api.service.IMmallCategoryService;
import com.zab.mmal.common.utils.JudgeUtil;
import com.zab.mmal.core.mapper.MmallCategoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zab
 * @since 2019-11-19
 */
@Service
public class MmallCategoryServiceImpl extends ServiceImpl<MmallCategoryMapper, MmallCategory> implements IMmallCategoryService {

    @Resource
    private ICategoryDao categoryDao;

    @Override
    @Transactional
    public boolean addCategory(String categoryName, Integer parentId) {
        MmallCategory mmallCategory = new MmallCategory();
        mmallCategory.setName(categoryName);
        mmallCategory.setParentId(parentId);
        mmallCategory.setStatus(true);
        return save(mmallCategory);
    }

    @Override
    @Transactional
    public boolean updateCategory(String categoryName, Integer categoryId) {
        MmallCategory mmallCategory = new MmallCategory();
        mmallCategory.setName(categoryName);
        mmallCategory.setId(categoryId);
        mmallCategory.setUpdateTime(new Date());
        return updateById(mmallCategory);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public List<MmallCategory> getChildrenParalleCategory(Integer parentId) {
        MmallCategory mmallCategory = new MmallCategory();
        mmallCategory.setParentId(parentId);
        QueryWrapper<MmallCategory> wrapper = new QueryWrapper<>(mmallCategory);
        return list(wrapper);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public List<Integer> getDeepCategory(Integer categoryId) {
        return categoryDao.getDeepCategory(categoryId);
    }

}
