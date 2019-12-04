package com.zab.mmal.manager.controller;

import com.zab.mmal.common.commons.ReturnData;
import com.zab.mmal.common.enums.SysCodeMsg;
import com.zab.mmal.common.utils.JudgeUtil;
import com.zab.mmal.common.annotions.AdminOperate;
import com.zab.mmal.manager.feign.ManagerFeignService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/manage/category")
@AdminOperate
public class MCategoryController {

    @Autowired
    private ManagerFeignService categoryFeignService;

    @PostMapping("addCategory")

    public ReturnData addCategory(HttpServletRequest request, @RequestParam String categoryName,
                                  @RequestParam(required = false, defaultValue = "0") Integer parentId) {
        if (StringUtils.isBlank(categoryName)) {
            return new ReturnData(SysCodeMsg.PARAM_IS_NULL.getCode(), "分类名称不能为空");
        }
        return categoryFeignService.addCategory(categoryName, parentId);
    }

    @PostMapping("updateCategory")
    public ReturnData updateCategory(HttpServletRequest request, @RequestParam String categoryName,
                                     @RequestParam Integer categoryId) {
        if (JudgeUtil.isNullOr(categoryName, categoryId)) {
            return new ReturnData(SysCodeMsg.PARAM_IS_NULL.getCode(), "分类名称或者主键为空");
        }
        return categoryFeignService.updateCategory(categoryName, categoryId);
    }

    @GetMapping("getChildrenParalleCategory/{parentId}")
    public ReturnData getChildrenParalleCategory(HttpServletRequest request, @PathVariable Integer parentId) {
        if (null == parentId) {
            return new ReturnData(SysCodeMsg.PARAM_IS_NULL.getCode(), "父级Id不能为空");
        }
        return categoryFeignService.getChildrenParalleCategory(parentId);
    }

    @GetMapping("getDeepCategory/{categoryId}")
    public ReturnData getDeepCategory(HttpServletRequest request, @PathVariable Integer categoryId) {
        if (null == categoryId) {
            return new ReturnData(SysCodeMsg.PARAM_IS_NULL.getCode(), "Id不能为空");
        }
        return categoryFeignService.getDeepCategory(categoryId);
    }

}
