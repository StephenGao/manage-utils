package com.pkit.advice;

import com.pkit.util.JSONUtils;
import org.apache.log4j.Logger;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by xiaoping on 2016/5/24.
 */
@Component
public class LogMethodAfterReturningAdvice implements AfterReturningAdvice {
    private static Logger logger = Logger.getLogger(LogMethodAfterReturningAdvice.class);

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        String param="";
        if(args != null && args.length>0) {
            StringBuffer stringBuffer=new StringBuffer();
            for(int i = 0; i < args.length; i++) {
                stringBuffer.append(args[i]);
            }
            param=stringBuffer.toString();
        }
        String retValue= JSONUtils.object2json(returnValue);
        logger.info("调用对象为["+target.getClass().getName()+"],调用方法为["+method.getName() + "],传入参数为[" + param + "],执行结果为[" + retValue+"]");
    }
}
