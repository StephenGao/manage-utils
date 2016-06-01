package com.pkit.plugin.mybatis;

/**
 * Created by xiaoping on 2015/11/5.
 */
public class MySQLDialect extends Dialect {


    @Override
    public String getLimitString(String sql, int offset, int limit) {
        sql = sql.trim();
        StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
        String beginrow = String.valueOf(offset);
        pagingSelect.append(sql);
        pagingSelect.append(" limit " + beginrow + "," + limit);
        return pagingSelect.toString();
    }
}
