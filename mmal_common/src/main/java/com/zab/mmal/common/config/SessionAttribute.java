package com.zab.mmal.common.config;


import com.zab.mmal.api.entity.MmallUser;

import javax.servlet.http.HttpSession;

public class SessionAttribute implements Constant {

    public static void currentUser(HttpSession session, MmallUser user) {
        session.setAttribute(CURRENT_USER, user);
    }

    public static MmallUser currentUser(HttpSession session) {
        return (MmallUser) session.getAttribute(CURRENT_USER);
    }

    public static void removeCurrentUser(HttpSession session) {
        session.removeAttribute(CURRENT_USER);
    }

}
