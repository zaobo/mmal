package com.zab.mmal.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zab.mmal.api.entity.MmallCategory;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zab
 * @since 2019-11-19
 */
public interface IMmallCategoryService extends IService<MmallCategory> {

    boolean addCategory(String categoryName, Integer parentId);

    boolean updateCategory(String categoryName, Integer categoryId);

    List<MmallCategory> getChildrenParalleCategory(Integer parentId);

    List<Integer> getDeepCategory(Integer categoryId);
}
