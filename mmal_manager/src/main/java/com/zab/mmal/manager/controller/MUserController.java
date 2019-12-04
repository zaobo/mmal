package com.zab.mmal.manager.controller;

import com.zab.mmal.api.entity.MmallUser;
import com.zab.mmal.common.commons.ReturnData;
import com.zab.mmal.common.config.Constant;
import com.zab.mmal.common.config.SessionAttribute;
import com.zab.mmal.common.enums.SysCodeMsg;
import com.zab.mmal.common.utils.Base64Util;
import com.zab.mmal.common.utils.JudgeUtil;
import com.zab.mmal.common.utils.MD5Utils;
import com.zab.mmal.manager.feign.ManagerFeignService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/manage/user/")
public class MUserController {

    @Autowired
    private ManagerFeignService managerFeignService;

    @PostMapping("login")
    public ReturnData login(HttpServletRequest request, @RequestParam String userName, @RequestParam String password) {
        MmallUser user = managerFeignService.login(userName);
        if (null == user) {
            return new ReturnData(SysCodeMsg.USERNAME_NOT_EXIST.getCode(), "用户不存在");
        }

        if (!StringUtils.equalsIgnoreCase(MD5Utils.MD5EncodeUtf8(Base64Util.strDecode(password)), user.getPassword())) {
            return new ReturnData(SysCodeMsg.PASSWORD_ERROR);
        }

        if (!JudgeUtil.isDBEq(user.getRole(), Constant.ROLE_ADMIN)) {
            return new ReturnData(SysCodeMsg.FAIL.getCode(), "不是管理员，无法登陆后台管理");
        }

        user.setPassword(StringUtils.EMPTY);
        SessionAttribute.currentUser(request.getSession(), user);
        return new ReturnData(user);
    }

}
