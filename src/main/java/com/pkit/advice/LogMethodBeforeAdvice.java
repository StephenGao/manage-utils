package com.pkit.advice;

import com.pkit.util.JSONUtils;
import org.apache.log4j.Logger;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by xiaoping on 2016/5/24.
 */
@Component
public class LogMethodBeforeAdvice implements MethodBeforeAdvice {
    private static Logger logger = Logger.getLogger(LogMethodBeforeAdvice.class);

    @Override
    public void before(Method arg0, Object[] arg1, Object arg2)
            throws Throwable {
        String param="";
        if(arg1 != null && arg1.length>0) {
            StringBuffer stringBuffer=new StringBuffer();
            for(int i = 0; i < arg1.length; i++) {
                stringBuffer.append(arg1[i]);
            }
            param=stringBuffer.toString();
        }
        logger.info("调用对象为["+arg2.getClass().getName()+"],调用方法为["+arg0.getName() + "],传入参数为[" + param + "]");
    }

}
