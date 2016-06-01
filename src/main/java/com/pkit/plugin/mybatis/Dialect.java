package com.pkit.plugin.mybatis;

/**
 * Created by xiaoping on 2015/11/5.
 */
public abstract class Dialect {

    public static enum Type {
        MYSQL,
        ORACLE
    }

    public abstract String getLimitString(String sql, int skipResults, int maxResults);
}
