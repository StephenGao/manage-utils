package com.pkit.filter;

import com.alibaba.druid.util.DruidWebUtils;
import com.alibaba.druid.util.PatternMatcher;
import com.alibaba.druid.util.ServletPathMatcher;
import net.sf.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by xiaoping on 2016/5/19.
 */
public class LogFilter implements Filter {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LogFilter.class);

    public static final String PARAM_NAME_EXCLUSIONS = "exclusions";
    private static Set<String> excludesPattern;
    protected String  contextPath;
    protected PatternMatcher pathMatcher= new ServletPathMatcher();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        HttpServletResponse response=(HttpServletResponse)servletResponse;
        String ip=request.getRemoteAddr();
        String url=request.getRequestURI();
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
        Map parameterMap=request.getParameterMap();
        if (isExclusion(url)) {
            chain.doFilter(request, response);
            return;
        }else {
            if (parameterMap != null && parameterMap.size() > 0)
                log.info("[UserIP]:" + ip + " [AccessUrl]:" + basePath + url + " [Params]:" + JSONObject.fromObject(parameterMap).toString());
            else
                log.info("[UserIP]:" + ip + " [AccessUrl]:" + basePath + url);
            long longStart = System.currentTimeMillis();
            chain.doFilter(request, response);
            long time = (System.currentTimeMillis() - longStart);
            log.info("[Execute Time]:" + time + " ms");
        }
    }

    public boolean isExclusion(String requestURI) {
        if (excludesPattern == null) {
            return false;
        }
        if (contextPath != null && requestURI.startsWith(contextPath)) {
            if(requestURI.lastIndexOf(".")!=-1) {
                requestURI = requestURI.substring(requestURI.lastIndexOf("."));
            }
        }

        for (String pattern : excludesPattern) {
            if (pathMatcher.matches(pattern, requestURI)) {
                return true;
            }
        }

        return false;
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String exclusions = filterConfig.getInitParameter(PARAM_NAME_EXCLUSIONS);
        if (exclusions != null && exclusions.trim().length() != 0) {
            excludesPattern = new HashSet<String>(Arrays.asList(exclusions.split("\\s*,\\s*")));
        }
        this.contextPath = DruidWebUtils.getContextPath(filterConfig.getServletContext());
    }

    @Override
    public void destroy() {

    }
}
