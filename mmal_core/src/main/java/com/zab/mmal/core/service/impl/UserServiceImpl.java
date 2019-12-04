package com.zab.mmal.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zab.mmal.api.entity.MmallUser;
import com.zab.mmal.api.service.IUserService;
import com.zab.mmal.common.exceptions.WrongArgumentException;
import com.zab.mmal.common.utils.Base64Util;
import com.zab.mmal.common.utils.JudgeUtil;
import com.zab.mmal.common.utils.MD5Utils;
import com.zab.mmal.common.config.Constant;
import com.zab.mmal.core.mapper.MmallUserMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl extends ServiceImpl<MmallUserMapper, MmallUser> implements IUserService {

    @Resource
    private MmallUserMapper mmallUserMapper;

    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    @Override
    public MmallUser login(String userName) {
        QueryWrapper<MmallUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userName);
        return getOne(queryWrapper);
    }

    @Override
    @Transactional
    public boolean register(MmallUser user) {
        QueryWrapper<MmallUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        Integer count = count(queryWrapper);
        if (count > 0) {
            throw new WrongArgumentException("用户已存在:{}", user.getUsername());
        }

        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", user.getEmail());
        count = count(queryWrapper);
        if (count > 0) {
            throw new WrongArgumentException("邮箱已存在:{}", user.getEmail());
        }

        user.setRole(Constant.ROLE_CUSTOMER);
        user.setPassword(MD5Utils.MD5EncodeUtf8(user.getPassword()));
        user.setCreateTime(new Date());
        return save(user);
    }

    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    @Override
    public String forgetGetQuestion(String userName) {
        QueryWrapper<MmallUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userName);
        Integer count = count(queryWrapper);
        if (JudgeUtil.isDBEq(count, 0)) {
            throw new WrongArgumentException("用户不存在:{}", userName);
        }

        return mmallUserMapper.getQuestion(userName);
    }

    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    @Override
    public String checkAnswer(String userName, String question, String answer) {
        QueryWrapper<MmallUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userName);
        queryWrapper.eq("question", question);
        queryWrapper.eq("answer", answer);
        Integer count = count(queryWrapper);
        if (count > 0) {
            return UUID.randomUUID().toString();
        } else {
            throw new WrongArgumentException("答案错误，请重新输入:{}", answer);
        }
    }

    @Override
    @Transactional
    public boolean updatePWDByUsername(String userName, String newPWD) {
        MmallUser user = new MmallUser();
        user.setPassword(MD5Utils.MD5EncodeUtf8(newPWD));
        user.setUpdateTime(new Date());
        UpdateWrapper<MmallUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("username", userName);
        return update(user, updateWrapper);
    }

    @Override
    @Transactional
    public boolean restPassword(String oldPassword, String newPassword, MmallUser user) {
        // 验证当前用户的密码
        QueryWrapper<MmallUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", user.getId());
        queryWrapper.eq("password", MD5Utils.MD5EncodeUtf8(oldPassword));
        int resultCount = count(queryWrapper);
        if (JudgeUtil.isDBEq(resultCount, 0)) {
            throw new WrongArgumentException("旧密码错误:{}", oldPassword);
        }

        user.setUpdateTime(new Date());
        user.setPassword(MD5Utils.MD5EncodeUtf8(newPassword));
        return updateById(user);
    }

    @Override
    @Transactional
    public MmallUser updateUserInfo(MmallUser user) {
        // 验证email是否存在，是否与别人的重复
        QueryWrapper<MmallUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("id", user.getId());
        queryWrapper.eq("email", user.getEmail());
        int resultCount = count(queryWrapper);
        if (resultCount > 0) {
            throw new WrongArgumentException("Email被占用:{}", user.getEmail());
        }

        // 不能修改用户名和密码,角色
        user.setPassword(null);
        user.setUsername(null);
        user.setRole(null);
        user.setUpdateTime(new Date());

        if (updateById(user)) {
            return user;
        }
        return null;
    }

    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    @Override
    public MmallUser getUserById(Integer id) {
        MmallUser user = getById(id);
        if (null != user) {
            user.setPassword(StringUtils.EMPTY);
        }
        return user;
    }
}
