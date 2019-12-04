package com.zab.mmal.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Slf4j
@Component
public class PreRequestFilter extends ZuulFilter {

    public static final String PRE_KEY = UUID.randomUUID().toString();

    @Override
    public String filterType() {
        // pre filter
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //获取当前请求的请求上下文
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        //记录请求进入时间
        request.setAttribute("api_request_time", System.currentTimeMillis());
        return null;
    }
}