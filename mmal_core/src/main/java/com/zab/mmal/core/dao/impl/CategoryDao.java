package com.zab.mmal.core.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.zab.mmal.api.dao.ICategoryDao;
import com.zab.mmal.api.entity.MmallCategory;
import com.zab.mmal.common.utils.JudgeUtil;
import com.zab.mmal.core.mapper.MmallCategoryMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class CategoryDao extends ServiceImpl<MmallCategoryMapper, MmallCategory> implements ICategoryDao {

    /**
     * 递归查询本节点id和子节点id
     * @param categoryId
     * @return
     */
    @Override
    public List<Integer> getDeepCategory(Integer categoryId) {
        Set<MmallCategory> categorySet = Sets.newHashSet();
        findChildCategory(categorySet, categoryId);

        List<Integer> list = Lists.newLinkedList();
        if (null != categoryId) {
            for (MmallCategory mmallCategory : categorySet) {
                list.add(mmallCategory.getId());
            }
        }
        return list;
    }

    // 递归算法
    private Set<MmallCategory> findChildCategory(Set<MmallCategory> categorySet, Integer categoryId) {
        MmallCategory mmallCategory = getById(categoryId);
        if (null != mmallCategory) {
            categorySet.add(mmallCategory);
        }

        // 查找子节点
        QueryWrapper<MmallCategory> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", categoryId);
        List<MmallCategory> list = list(wrapper);

        if (!JudgeUtil.isEmpty(list)) {
            for (MmallCategory category : list) {
                findChildCategory(categorySet, category.getId());
            }
        }
        return categorySet;
    }
}
