package com.java.mobile.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.java.mobile.common.utils.SerialNumber;
import com.java.mobile.common.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author xdd
 * @date 2018/8/13
 */
public class LoginInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        StringBuffer requestURL = request.getRequestURL();
        LOGGER.info(method + "=======" + requestURL);
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }
        String ticket = request.getHeader("ticket");
        if (StringUtils.isNotBlank(ticket)) {
            HttpSession session = request.getSession();
            session.setAttribute("userId", ticket);
            return true;
        } else {
            response.getOutputStream().write(JSONObject.toJSONString(new Result<>(401)).getBytes());
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
