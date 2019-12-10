package com.zab.mmal.common.aspect;

import com.zab.mmal.api.entity.MmallUser;
import com.zab.mmal.common.annotions.AdminOperate;
import com.zab.mmal.common.config.Constant;
import com.zab.mmal.common.config.SessionAttribute;
import com.zab.mmal.common.exceptions.NoAuthException;
import com.zab.mmal.common.utils.JudgeUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class CheckIsAdmin {

    /**
     * 定义切点 @Pointcut
     * 在注解的位置切入代码
     */
    @Pointcut("@annotation(com.zab.mmal.common.annotions.AdminOperate)||@within(com.zab.mmal.common.annotions.AdminOperate)")
    public void isAdminCut() {
    }

    @Before("isAdminCut()")
    public void doJudge(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> clazz = method.getDeclaringClass();
        AdminOperate adminOperate = method.getAnnotation(AdminOperate.class);
        if (null == adminOperate) {
            adminOperate = clazz.getAnnotation(AdminOperate.class);
        }

        if (null != adminOperate) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();
            MmallUser currentUser = SessionAttribute.currentUser(request.getSession());
            if (null == currentUser) {
                throw new NoAuthException("用户未登录");
            }
            if (!JudgeUtil.isDBEq(currentUser.getRole(), Constant.ROLE_ADMIN)) {
                throw new NoAuthException("不是管理员");
            }
        }

    }

}
