package com.pkit.common;

import java.util.List;

/**
 * Created by xiaoping on 2015/12/4.
 */
public interface EntityDao<E,PK> {



    public int deleteById(PK id);

    public int deleteByIds(List<Long> list);

    public Object save(E entity);

    public int saveBatch(List<E> list);

    public int saveOrUpdate(E entity);

    public int update(E entity);

    public int updateBatch(List<E> list);

    public Object findById(PK id);

    public Object findByQuery(BaseQuery baseQuery);

    public Page<E> findPage(BaseQuery baseQuery);

    public List<E> findAll();
}
