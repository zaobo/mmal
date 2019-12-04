package com.zab.mmal.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zab.mmal.api.entity.MmallUser;

public interface IUserService extends IService<MmallUser> {

    MmallUser login(String userName);

    boolean register(MmallUser user);

    String  forgetGetQuestion(String userName);

    String checkAnswer(String userName, String question, String answer);

    boolean updatePWDByUsername(String userName, String newPWD);

    boolean restPassword(String oldPassword, String newPassword, MmallUser user);

    MmallUser updateUserInfo(MmallUser user);

    MmallUser getUserById(Integer id);
}
