package com.pkit.common;

import java.io.Serializable;

/**
 * 分页请求信息
 * 其中范型<T>为filters的类型
 * 已经过时,使用PageQuery替代
 *
 */
@SuppressWarnings("serial")
public class PageRequest<T> implements Serializable {

	public static final int DEFAULT_PAGE_SIZE = 10;
	
	/**
	 * 过滤参数
	 */
	@Deprecated
	private T filters;
	/**
	 * 页号码,页码从1开始
	 */
	private int pageNumber;
	/**
	 * 分页大小
	 */
	private int pageSize = DEFAULT_PAGE_SIZE;
	/**
	 * 排序的多个列,如: username desc
	 */
	private String sortColumns;

    private int totalCount;

    private int totalPage;
    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public PageRequest() {
	}
	
	@Deprecated
	public PageRequest(T filters) {
		setFilters(filters);
	}
	
	public PageRequest(int pageNumber, int pageSize) {
		this(pageNumber,pageSize,(T)null);
	}
	
	@Deprecated
	public PageRequest(int pageNumber, int pageSize, T filters) {
		this(pageNumber,pageSize,filters,null);
	}
	
	public PageRequest(int pageNumber, int pageSize,String sortColumns) {
		this(pageNumber,pageSize,null,sortColumns);
	}
	
	@Deprecated
	public PageRequest(int pageNumber, int pageSize, T filters,String sortColumns) {
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		setFilters(filters);
		setSortColumns(sortColumns);
	}
	
	@Deprecated
	public T getFilters() {
		return filters;
	}

	@Deprecated
	public void setFilters(T filters) {
		this.filters = filters;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public String getSortColumns() {
		return sortColumns;
	}
	/**
	 * 排序的列,可以同时多列,使用逗号分隔,如 username desc,age asc
	 * @return
	 */
	public void setSortColumns(String sortColumns) {
		checkSortColumnsSqlInjection(sortColumns);
		if(sortColumns != null && sortColumns.length() > 200) {
			throw new IllegalArgumentException("sortColumns.length() <= 200 must be true");
		}
		this.sortColumns = sortColumns;
	}


	private void checkSortColumnsSqlInjection(String sortColumns) {
		if(sortColumns == null) return;
		if(sortColumns.indexOf("'") >= 0 || sortColumns.indexOf("\\") >= 0) {
			throw new IllegalArgumentException("sortColumns:"+sortColumns+" has SQL Injection risk");
		}
	}
}
