package com.pkit.listener;

import com.pkit.common.SpringContextUtil;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 说明：在服务启动时，加入spring的配置文件，这样，在其他地方要用spring管理的对象时，直接调用即可，不用再加载spring的配置文件
 * */
public class SpringCtxListener implements ServletContextListener{
	public void contextDestroyed(ServletContextEvent arg0) {
	}
	public void contextInitialized(ServletContextEvent arg0) {
        SpringContextUtil.initSpringFromWebContext(WebApplicationContextUtils.getWebApplicationContext(arg0.getServletContext()));
	}
}
