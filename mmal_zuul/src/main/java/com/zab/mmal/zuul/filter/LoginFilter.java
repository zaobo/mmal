package com.zab.mmal.zuul.filter;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.zab.mmal.api.entity.MmallUser;
import com.zab.mmal.common.commons.ReturnData;
import com.zab.mmal.common.config.Constant;
import com.zab.mmal.common.enums.SysCodeMsg;
import com.zab.mmal.common.exceptions.ProjectException;
import com.zab.mmal.common.utils.JudgeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

@Component
@Slf4j
public class LoginFilter extends ZuulFilter {

    @Resource
    private SessionRepository<Session> sessionRepository;
    @Value("${mmal.white.list}")
    private String whiteList;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String path = request.getRequestURI();
        if (StringUtils.startsWithIgnoreCase(path, "/service/") || StringUtils.startsWithIgnoreCase(path, "/manage/")) {
            List<String> list = this.getWhiteList();
            if (!JudgeUtil.isEmpty(list)) {
                for (String pre : list) {
                    if (path.matches(pre)) {
                        //能匹配上正则，就是在白名单中，不需要拦截
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        List<Session> sessions = findSession(request, this.sessionRepository);

        MmallUser currentUser = null;
        if (!JudgeUtil.isEmpty(sessions)) {
            for (Session s : sessions) {
                currentUser = s.getAttribute(Constant.CURRENT_USER);
                if (null != currentUser) {
                    break;
                }
            }
        }
        if (null == currentUser) {
            log.info("请求没有权限，被拦截");
            return this.sendError(ctx, SysCodeMsg.NO_LOGIN.getCode(), SysCodeMsg.NO_LOGIN.getDescribe());
        }

        // 登录过了
        return this.sendSuccess(ctx, currentUser);
    }

    private Object sendSuccess(RequestContext ctx, MmallUser currentUser) throws ZuulException {
        if (null != currentUser) {
            try {
                ctx.addZuulRequestHeader("userId", URLEncoder.encode(currentUser.getId() + "", "UTF-8"));
                if (StringUtils.isNotBlank(currentUser.getUsername())) {
                    ctx.addZuulRequestHeader("username", URLEncoder.encode(currentUser.getUsername(), "UTF-8"));
                }
                if (StringUtils.isNotBlank(currentUser.getPhone())) {
                    ctx.addZuulRequestHeader("phone", URLEncoder.encode(currentUser.getPhone(), "UTF-8"));
                }
                if (StringUtils.isNotBlank(currentUser.getEmail())) {
                    ctx.addZuulRequestHeader("email", URLEncoder.encode(currentUser.getEmail(), "UTF-8"));
                }
                ctx.addZuulRequestHeader("role", URLEncoder.encode(currentUser.getRole() + "", "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new ProjectException("编码错误", e);
            }
        }
        return null;
    }

    private Object sendError(RequestContext ctx, int code, String message) {
        //直接返回客户端，不进行下一步接口调用
        HttpServletResponse response = ctx.getResponse();
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        ReturnData returnData = new ReturnData(code, message);
        ctx.setResponseBody(JSON.toJSONString(returnData));
        ctx.setSendZuulResponse(false);
        return null;
    }

    public static List<Session> findSession(HttpServletRequest request, SessionRepository<Session> sessionRepository) {
        HttpSession httpSession = request.getSession(false);
        List<String> sessionids = new LinkedList<>();
        if (null != httpSession) {
            String sessionid = httpSession.getId();
            if (StringUtils.isNotBlank(sessionid)) {
                if (!sessionids.contains(sessionid)) {
                    sessionids.add(sessionid);
                }
            }
        }
        //先从cookie中取
        if (!JudgeUtil.isEmpty(request.getCookies())) {
            for (Cookie cookie : request.getCookies()) {
                if (StringUtils.containsIgnoreCase(cookie.getName(), "SESSION")) {
                    String sessionid = cookie.getValue().trim();
                    if (!sessionids.contains(sessionid)) {
                        sessionids.add(sessionid);
                    }
                }
            }
        }

        List<Session> list = new LinkedList<>();
        for (String sessionid : sessionids) {
            Session session = sessionRepository.findById(sessionid);
            if (null != session && !session.isExpired()) {
                list.add(session);
            }
        }
        return list;
    }

    private List<String> getWhiteList() {
        return Lists.newArrayList(StringUtils.split(whiteList, ","));
    }

}
