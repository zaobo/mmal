package com.zab.mmal.protal.controller;

import com.zab.mmal.api.entity.MmallUser;
import com.zab.mmal.common.commons.ReturnData;
import com.zab.mmal.common.config.SessionAttribute;
import com.zab.mmal.common.enums.SysCodeMsg;
import com.zab.mmal.common.utils.Base64Util;
import com.zab.mmal.common.utils.JudgeUtil;
import com.zab.mmal.common.utils.MD5Utils;
import com.zab.mmal.protal.fegin.ProtalFeignService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/service/user")
public class PUserController {

    @Autowired
    private ProtalFeignService protalFeignService;

    @PostMapping("login")
    public ReturnData login(HttpServletRequest request, @RequestParam String userName, @RequestParam String password) {
        MmallUser currentUser = SessionAttribute.currentUser(request.getSession());
        if (null != currentUser) {
            return new ReturnData(currentUser);
        }
        MmallUser user = protalFeignService.login(userName);
        if (null == user) {
            return new ReturnData(SysCodeMsg.USERNAME_NOT_EXIST.getCode(), "用户不存在");
        }

        if (!StringUtils.equalsIgnoreCase(MD5Utils.MD5EncodeUtf8(Base64Util.strDecode(password)), user.getPassword())) {
            return new ReturnData(SysCodeMsg.PASSWORD_ERROR.getCode(), "密码错误");
        }

        user.setPassword(StringUtils.EMPTY);
        SessionAttribute.currentUser(request.getSession(), user);
        return new ReturnData(user);
    }

    @PostMapping("register")
    public ReturnData register(HttpServletRequest request, @RequestBody MmallUser user) {
        return protalFeignService.register(user);
    }

    @PostMapping("forgetGetQuestion")
    public ReturnData forgetGetQuestion(HttpServletRequest request, @RequestParam String userName) {
        return new ReturnData(protalFeignService.forgetGetQuestion(userName));
    }

    @PostMapping("checkAnswer")
    public ReturnData checkAnswer(HttpServletRequest request, @RequestParam String userName, @RequestParam String question, @RequestParam String answer) {
        return new ReturnData(protalFeignService.checkAnswer(userName, question, answer));
    }

    @PostMapping("forgetRestPassword")
    public ReturnData forgetRestPassword(HttpServletRequest request, @RequestParam String userName, @RequestParam String newPWD, @RequestParam String forgetToken) {
        return protalFeignService.forgetRestPassword(userName, newPWD, forgetToken);
    }

    @PostMapping("restPassword")
    public ReturnData restPassword(HttpServletRequest request, @RequestParam String oldPassword, @RequestParam String newPassword) {
        MmallUser currentUser = SessionAttribute.currentUser(request.getSession());
        return protalFeignService.restPassword(oldPassword, newPassword, currentUser);
    }

    @PostMapping("updateUserInfo")
    public ReturnData updateUserInfo(HttpServletRequest request, @RequestBody MmallUser user) {
        MmallUser currentUser = SessionAttribute.currentUser(request.getSession());
        user.setId(currentUser.getId());
        MmallUser updateUser = protalFeignService.updateUserInfo(user);
        if (null != updateUser) {
            // 更新session中的用户信息
            updateUser.setUsername(currentUser.getUsername());
            updateUser.setRole(currentUser.getRole());
            SessionAttribute.currentUser(request.getSession(), updateUser);
            return new ReturnData();
        }
        return new ReturnData(SysCodeMsg.FAIL);
    }

    @PostMapping("getUserById")
    public ReturnData getUserById(HttpServletRequest request, @RequestParam(required = false) Integer id) {
        MmallUser currentUser = SessionAttribute.currentUser(request.getSession());
        MmallUser user = protalFeignService.getUserById(!JudgeUtil.isDBNull(id) ? id : null != currentUser ? currentUser.getId() : null);
        if (null == user) {
            return new ReturnData(SysCodeMsg.FAIL);
        }
        return new ReturnData(user);
    }

    @PostMapping("logout")
    public ReturnData logout(HttpServletRequest request) {
        SessionAttribute.removeCurrentUser(request.getSession());
        return new ReturnData();
    }

    @PostMapping("getUserInfo")
    public ReturnData getUserInfo(HttpServletRequest request) {
        MmallUser user = SessionAttribute.currentUser(request.getSession());
        if (null != user) {
            return new ReturnData(user);
        }
        return new ReturnData(SysCodeMsg.NO_LOGIN);
    }

}
