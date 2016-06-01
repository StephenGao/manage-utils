package com.pkit.common;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhangxp
 */
public abstract class BaseManager<E, PK extends Serializable> {

    protected Log log = LogFactory.getLog(getClass());

    protected abstract EntityDao getEntityDao();

    public boolean save(E entity) {
        return getEntityDao().save(entity) != null ? true : false;
    }

    public boolean saveBatch(List list) {
        return getEntityDao().saveBatch(list) >0 ? true : false;
    }

    public boolean deleteById(PK id) {
        return getEntityDao().deleteById(id) > 0 ? true : false;
    }

    public boolean deleteByIds(List<Long> list) {
        return getEntityDao().deleteByIds(list) > 0 ? true : false;
    }

    public boolean update(E entity) throws DataAccessException {
        return getEntityDao().update(entity) > 0 ? true : false;
    }

    public boolean updateBatch(List<E> list) throws DataAccessException {
        return getEntityDao().updateBatch(list) > 0 ? true : false;
    }

    public boolean saveOrUpdate(E entity) throws DataAccessException {
        return getEntityDao().saveOrUpdate(entity) > 0 ? true : false;
    }

    public E findById(PK id) {
        return (E) getEntityDao().findById(id);
    }

    public List<E> findAll() throws DataAccessException {
        return getEntityDao().findAll();
    }

    public Object findByQuery(BaseQuery baseQuery){
        return  (E)getEntityDao().findByQuery(baseQuery);
    }

    public Page<E> findPage(BaseQuery baseQuery){
        return  (Page<E>)getEntityDao().findPage(baseQuery);
    }

}
