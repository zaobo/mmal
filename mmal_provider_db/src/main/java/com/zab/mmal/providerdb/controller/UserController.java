package com.zab.mmal.providerdb.controller;


import com.zab.mmal.api.entity.MmallUser;
import com.zab.mmal.api.service.IUserService;
import com.zab.mmal.common.commons.ReturnData;
import com.zab.mmal.common.config.Constant;
import com.zab.mmal.common.enums.SysCodeMsg;
import com.zab.mmal.common.exceptions.WrongArgumentException;
import com.zab.mmal.common.utils.JudgeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/provider/user/")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;
    @Resource(name = "stringObjectRedisTemplate")
    private RedisTemplate<String, String> stringObjectRedisTemplate;
    @Value("${redis.minutes.timeout}")
    private Integer redisMinutesTimeout;

    @PostMapping("login")
    public MmallUser login(@RequestParam String userName) {
        return userService.login(userName);
    }

    @PostMapping("register")
    public ReturnData register(@RequestBody MmallUser user) {
        boolean ok = userService.register(user);
        return new ReturnData(ok ? SysCodeMsg.SUCCESS : SysCodeMsg.USER_REGISTER_FAIL);
    }

    @PostMapping("forgetGetQuestion")
    public String forgetGetQuestion(@RequestParam String userName) {
        return userService.forgetGetQuestion(userName);
    }

    @PostMapping("checkAnswer")
    public String checkAnswer(@RequestParam String userName, @RequestParam String question, @RequestParam String answer) {
        String forgetToken = userService.checkAnswer(userName, question, answer);
        stringObjectRedisTemplate.opsForValue().set(Constant.TOKEN_PRFIX + userName, forgetToken, redisMinutesTimeout, TimeUnit.MINUTES);
        return forgetToken;
    }

    @PostMapping("forgetRestPassword")
    public ReturnData forgetRestPassword(@RequestParam String userName, @RequestParam String newPWD, @RequestParam String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return new ReturnData(SysCodeMsg.UPDATE_FAIL.getCode(), "token未传");
        }

        String cacheToken = stringObjectRedisTemplate.opsForValue().get(Constant.TOKEN_PRFIX + userName);
        if (StringUtils.isBlank(cacheToken)) {
            return new ReturnData(SysCodeMsg.TOKEN_IS_NULL);
        }

        if (!StringUtils.equalsIgnoreCase(forgetToken, cacheToken)) {
            return new ReturnData(SysCodeMsg.TOKEN_ERROR);
        }

        boolean ok = userService.updatePWDByUsername(userName, newPWD);
        if (ok) {
            stringObjectRedisTemplate.delete(Constant.TOKEN_PRFIX + userName);
            return new ReturnData(SysCodeMsg.SUCCESS);
        }
        log.error("修改密码失败");
        return new ReturnData(SysCodeMsg.MODIFY_PWD_FAIL);
    }

    @PostMapping("restPassword")
    public ReturnData restPassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestBody MmallUser currentUser) {
        boolean ok = userService.restPassword(oldPassword, newPassword, currentUser);
        return new ReturnData(ok ? SysCodeMsg.SUCCESS : SysCodeMsg.MODIFY_PWD_FAIL);
    }

    @PostMapping("updateUserInfo")
    public MmallUser updateUserInfo(@RequestBody MmallUser user) {
        MmallUser updateUser = userService.updateUserInfo(user);
        if (null == updateUser) {
            throw new WrongArgumentException("修改信息失败:{}", user.getUsername());
        }
        return updateUser;
    }

    @PostMapping("getUserById")
    public MmallUser getUserById(@RequestParam(required = false) Integer id) {
        if (JudgeUtil.isDBNull(id)) {
            return null;
        }
        return userService.getUserById(id);
    }

}
