package com.pkit.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * Spring 上下文辅助操作对象，用于人工初始化或者全局自动初始化
 *
 * @author
 */
public class SpringContextUtil implements ApplicationContextAware {
    private static final Log logger = LogFactory.getLog(SpringContextUtil.class);
    private static ApplicationContext applicationContext = null;     //Spring应用上下文环境

    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     *
     * @param applicationContext
     * @throws org.springframework.beans.BeansException
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取对象
     *
     * @param name
     * @return Object 一个以所给名字注册的bean的实例
     * @throws org.springframework.beans.BeansException
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /**
     * 获取类型为requiredType的对象
     * 如果bean不能被类型转换，相应的异常将会被抛出（BeanNotOfRequiredTypeException）
     *
     * @param name         bean注册名
     * @param requiredType 返回对象类型
     * @return Object 返回requiredType类型对象
     * @throws org.springframework.beans.BeansException
     */
    public static Object getBean(String name, Class requiredType) throws BeansException {
        return applicationContext.getBean(name, requiredType);
    }

    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     *
     * @param name
     * @return boolean
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。
     * 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
     *
     * @param name
     * @return boolean
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
     */
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.isSingleton(name);
    }

    /**
     * @param name
     * @return Class 注册对象的类型
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
     */
    public static Class getType(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.getType(name);
    }

    /**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     *
     * @param name
     * @return
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
     */
    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.getAliases(name);
    }

    /**
     * 直接初始化Spring，用于Testcase或者独立应用程序
     *
     * @return
     */
    public static boolean initSpring(String xmlfile) {
        if (applicationContext != null) {
            return true;
        }
        try {
            logger.info("分析spring配置文件");
            applicationContext = new GenericApplicationContext();
            XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader((GenericApplicationContext) applicationContext);
            xmlReader.loadBeanDefinitions(new ClassPathResource(xmlfile));
            logger.info("初始化spring配置的Bean");
            ((GenericApplicationContext) applicationContext).refresh();
        } catch (Exception e) {
            logger.error("初始化Spring-Framework失败", e);
            throw new RuntimeException("初始化Spring-Framework失败", e);
        }

        if (logger.isInfoEnabled()) {
            String str[] = applicationContext.getBeanDefinitionNames();
            logger.info((new StringBuilder("共初始化")).append(str.length).append("个beans,分别为:").toString());
            for (int i = 0; i < str.length; i++)
                logger.info((new StringBuilder("        ")).append(str[i]).toString());

            logger.info("初始化spring配置文件成功");
        }


        return true;
    }

    /**
     * 检查Spring状态，是否上下文初始化好了
     */
    public static void checkStatus() {
        if (applicationContext == null) {
            logger.warn("spring还没有初始化或者初始化失败");
            throw new RuntimeException("spring还没有初始化或者初始化失败");
        }
        logger.info("初始化spring-hibernate-db成功");
    }

    /**
     * 增加新的xml配置文件到Spring 上下文中
     *
     * @param xmlfile
     * @return
     */
    public static boolean addSpringBeanDefineInClassPath(String xmlfile) {
        if (applicationContext == null) {
            return false;
        }
        GenericApplicationContext newctx = null;
        try {
            newctx = new GenericApplicationContext();
            logger.info("追加Srping配置文件:" + xmlfile);
            XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(newctx);
            xmlReader.loadBeanDefinitions(new ClassPathResource(xmlfile));
            newctx.setParent(applicationContext);
            newctx.refresh();
            applicationContext = newctx;
        } catch (Exception e) {
            logger.error("追加Srping配置文件失败", e);
            return false;
        }

        return true;
    }

    /**
     * 根据已有的上下文对象初始化SpringContextUtil
     *
     * @param ctxWeb
     */
    public static void initSpringFromWebContext(ApplicationContext ctxWeb) {
        applicationContext = ctxWeb;
    }

    /**
     * 清除上spring 上下文，一般只用于在测试用例或者独立应用程序中
     */
    public static void cleanup() {
        applicationContext = null;
    }
}
