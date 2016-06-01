package com.pkit.plugin.mybatis;


import org.apache.ibatis.executor.parameter.DefaultParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

/**
 * Created by xiaoping on 2015/11/5.
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class PaginationInterceptor implements Interceptor {

    private static final Log logger = LogFactory.getLog(PaginationInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
        MetaObject metaStatementHandler = MetaObject.forObject(statementHandler);

        RowBounds rowBounds = (RowBounds)metaStatementHandler.getValue("delegate.rowBounds");
        if(rowBounds == null || rowBounds == RowBounds.DEFAULT){
            return invocation.proceed();
        }

        DefaultParameterHandler defaultParameterHandler = (DefaultParameterHandler)metaStatementHandler.getValue("delegate.parameterHandler");
        Map parameterMap = (Map)defaultParameterHandler.getParameterObject();
        Object sidx = parameterMap.get("_sidx");
        Object sord = parameterMap.get("_sord");
        String originalSql = (String)metaStatementHandler.getValue("delegate.boundSql.sql");
        if(sidx != null && sord != null){
            originalSql = originalSql + " order by " + sidx + " " + sord;
        }

        Configuration configuration = (Configuration)metaStatementHandler.getValue("delegate.configuration");
        Dialect.Type databaseType = Dialect.Type.valueOf(configuration.getVariables().getProperty("dialect").toUpperCase());

        if(databaseType == null){
            throw new RuntimeException("the value of the dialect property in configuration.xml is not defined : " + configuration.getVariables().getProperty("dialect"));
        }
        Dialect dialect = null;
        switch(databaseType){
            case ORACLE:
                dialect = new OracleDialect();
                break;
            case MYSQL:
                dialect = new MySQLDialect();
                break;

        }
        metaStatementHandler.setValue("delegate.boundSql.sql", dialect.getLimitString(originalSql, rowBounds.getOffset(), rowBounds.getLimit()) );
        metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET );
        metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT );

        logger.debug("生成分页SQL : " + statementHandler.getBoundSql().getSql());
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
