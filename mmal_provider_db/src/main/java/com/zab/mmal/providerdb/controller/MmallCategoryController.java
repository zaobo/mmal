package com.zab.mmal.providerdb.controller;


import com.zab.mmal.api.entity.MmallCategory;
import com.zab.mmal.api.service.IMmallCategoryService;
import com.zab.mmal.common.commons.ReturnData;
import com.zab.mmal.common.enums.SysCodeMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zab
 * @since 2019-11-19
 */
@RestController
@RequestMapping("/provider/category")
public class MmallCategoryController {

    @Autowired
    private IMmallCategoryService categoryService;

    @PostMapping("addCategory")
    public ReturnData addCategory(@RequestParam String categoryName, @RequestParam Integer parentId) {
        boolean ok = categoryService.addCategory(categoryName, parentId);
        return new ReturnData(ok ? SysCodeMsg.SUCCESS : SysCodeMsg.FAIL);
    }

    @PostMapping("updateCategory")
    public ReturnData updateCategory(@RequestParam String categoryName, @RequestParam Integer categoryId) {
        boolean ok = categoryService.updateCategory(categoryName, categoryId);
        return new ReturnData(ok ? SysCodeMsg.SUCCESS : SysCodeMsg.UPDATE_FAIL);
    }

    @GetMapping("getChildrenParalleCategory/{parentId}")
    public ReturnData getChildrenParalleCategory(@PathVariable Integer parentId) {
        return new ReturnData(categoryService.getChildrenParalleCategory(parentId));
    }

    @GetMapping("getDeepCategory/{categoryId}")
    public ReturnData getDeepCategory(@PathVariable Integer categoryId) {
        return new ReturnData(categoryService.getDeepCategory(categoryId));
    }

}
