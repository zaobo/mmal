package com.zab.mmal.api.dao;

import com.zab.mmal.api.entity.MmallCategory;
import java.util.List;

public interface ICategoryDao {

    List<Integer> getDeepCategory(Integer categoryId);

}
