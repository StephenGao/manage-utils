package com.pkit.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pkit.util.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.dao.support.DaoSupport;
import org.springframework.util.Assert;


/**
 * @author HOK
 * @version 1.0
 */
public abstract class BaseIbatis3Dao<E, PK extends Serializable> extends DaoSupport implements EntityDao<E, PK> {
    protected final Log log = LogFactory.getLog(getClass());

    private SqlSessionFactory sqlSessionFactory;
    private SqlSessionTemplate sqlSessionTemplate;

    protected void checkDaoConfig() throws IllegalArgumentException {
        Assert.notNull(sqlSessionFactory, "sqlSessionFactory must be not null");
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
    }

    public SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    @Override
    public Object findById(PK primaryKey) {
        Object object = getSqlSessionTemplate().selectOne(getIbatisMapperNamesapce() + ".findById", primaryKey);
        return object;
    }

    @Override
    public Object findByQuery(BaseQuery baseQuery) {
        return this.getSqlSessionTemplate().selectOne(getIbatisMapperNamesapce() + ".findByQuery", baseQuery);
    }

    @Override
    public Page<E> findPage(BaseQuery baseQuery) {
        return pageQuery(getIbatisMapperNamesapce() + ".findPage", baseQuery);
    }

    @Override
    public int deleteById(PK id) {
        return getSqlSessionTemplate().delete(getIbatisMapperNamesapce() + ".delete", id);
    }

    @Override
    public int deleteByIds(List<Long> list) {
        return getSqlSessionTemplate().delete(getIbatisMapperNamesapce() + ".deleteByIds", list);
    }

    @Override
    public Object save(E entity) {
        prepareObjectForSaveOrUpdate(entity);
        return getSqlSessionTemplate().insert(getIbatisMapperNamesapce() + ".insert", entity);
    }

    @Override
    public int saveBatch(List<E> list) {
        int affectCount = getSqlSessionTemplate().insert(getIbatisMapperNamesapce() + ".insertBatch", list);
        return affectCount;
    }

    public int update(E entity) {
        prepareObjectForSaveOrUpdate(entity);
        return getSqlSessionTemplate().update(getIbatisMapperNamesapce() + ".update", entity);
    }

    @Override
    public int updateBatch(List<E> list) {
        return getSqlSessionTemplate().update(getIbatisMapperNamesapce() + ".updateBatch", list);
    }


    /**
     * 用于子类覆盖,在insert,update之前调用
     *
     * @param o
     */
    protected void prepareObjectForSaveOrUpdate(E o) {
    }

    public String getIbatisMapperNamesapce() {
        throw new RuntimeException("not yet implement");
    }


    public String getCountStatementForPaging(String statementName) {
        return statementName + ".count";
    }

    protected Page pageQuery(String statementName, PageRequest pageRequest) {
        return pageQuery(getSqlSessionTemplate(), statementName, getCountStatementForPaging(statementName), pageRequest);
    }

    public static Page pageQuery(SqlSessionTemplate sqlSessionTemplate, String statementName, String countStatementName, PageRequest pageRequest) {
        Number totalCount = (Number) sqlSessionTemplate.selectOne(countStatementName, pageRequest);
        if (totalCount == null || totalCount.longValue() <= 0) {
            return new Page(pageRequest, 0);
        }

        Page page = new Page(pageRequest, totalCount.intValue());

        //其它分页参数,用于不喜欢或是因为兼容性而不使用方言(Dialect)的分页用户使用. 与getSqlMapClientTemplate().queryForList(statementName, parameterObject)配合使用
        Map filters = new HashMap();
        filters.put("offset", page.getFirstResult());
        filters.put("pageSize", page.getPageSize());
        filters.put("lastRows", page.getFirstResult() + page.getPageSize());
        filters.put("sortColumns", pageRequest.getSortColumns());

        Map parameterObject = PropertyUtils.describe(pageRequest);
        filters.putAll(parameterObject);
        List list = sqlSessionTemplate.selectList(statementName, filters, page.getFirstResult(), page.getPageSize());
        page.setRows(list);
        return page;
    }

    public List findAll() {
        throw new UnsupportedOperationException();
    }


    public static class SqlSessionTemplate {
        SqlSessionFactory sqlSessionFactory;

        public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
            this.sqlSessionFactory = sqlSessionFactory;
        }

        public Object execute(SqlSessionCallback action) {
            SqlSession session = null;
            try {
                session = sqlSessionFactory.openSession();
                Object result = action.doInSession(session);
                return result;
            } finally {
                if (session != null) session.close();
            }
        }

        public Object selectOne(final String statement, final Object parameter) {
            return execute(new SqlSessionCallback() {
                public Object doInSession(SqlSession session) {
                    return session.selectOne(statement, parameter);
                }
            });
        }

        public List selectList(final String statement, final Object parameter, final int offset, final int limit) {
            return (List) execute(new SqlSessionCallback() {
                public Object doInSession(SqlSession session) {
                    return session.selectList(statement, parameter, new RowBounds(offset, limit));
                }
            });
        }

        public List selectList(final String statement, final Object parameter) {
            return (List) execute(new SqlSessionCallback() {
                public Object doInSession(SqlSession session) {
                    return session.selectList(statement, parameter);
                }
            });
        }

        public int delete(final String statement, final Object parameter) {
            return (Integer) execute(new SqlSessionCallback() {
                public Object doInSession(SqlSession session) {
                    return session.delete(statement, parameter);
                }
            });
        }

        public int update(final String statement, final Object parameter) {
            return (Integer) execute(new SqlSessionCallback() {
                public Object doInSession(SqlSession session) {
                    return session.update(statement, parameter);
                }
            });
        }

        public int insert(final String statement, final Object parameter) {
            return (Integer) execute(new SqlSessionCallback() {
                public Object doInSession(SqlSession session) {
                    return session.insert(statement, parameter);
                }
            });
        }
    }

    public static interface SqlSessionCallback {
        public Object doInSession(SqlSession session);

    }


}
