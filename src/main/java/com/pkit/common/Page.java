package com.pkit.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * <pre>
 * 分页信息
 * 第一页从1开始
 * 
 * 已经过时,使用PageList替代
 * </pre>
 * 
 * @author HOK
 */
public class Page<T> implements Serializable {

	protected List<T> rows;

	protected int pageSize;

	protected int pageNumber;

	protected int total = 0;

	public Page(PageRequest p, int totalCount) {
		this(p.getPageNumber(), p.getPageSize(), totalCount);
	}

	public Page(int pageNumber, int pageSize, int totalCount) {
		this(pageNumber, pageSize, totalCount, new ArrayList(0));
	}

	public Page(int pageNumber, int pageSize, int totalCount, List<T> result) {
		if (pageSize <= 0)
			throw new IllegalArgumentException("[pageSize] must great than zero");
		this.pageSize = pageSize;
		this.pageNumber = PageUtils.computePageNumber(pageNumber, pageSize, totalCount);
		this.total = totalCount;
		setRows(result);
	}

	public void setRows(List<T> elements) {
		if (elements == null)
			throw new IllegalArgumentException("'result' must be not null");
		this.rows = elements;
	}

	/**
	 * 当前页包含的数据
	 *
	 * @return 当前页数据源
	 */
	public List<T> getRows() {
		return rows;
	}

	/**
	 * 是否是首页（第一页），第一页页码为1
	 *
	 * @return 首页标识
	 */
	public boolean isFirstPage() {
		return getThisPageNumber() == 1;
	}

	/**
	 * 是否是最后一页
	 *
	 * @return 末页标识
	 */
	public boolean isLastPage() {
		return getThisPageNumber() >= getLastPageNumber();
	}

	/**
	 * 是否有下一页
	 *
	 * @return 下一页标识
	 */
	public boolean isHasNextPage() {
		return getLastPageNumber() > getThisPageNumber();
	}

	/**
	 * 是否有上一页
	 *
	 * @return 上一页标识
	 */
	public boolean isHasPreviousPage() {
		return getThisPageNumber() > 1;
	}

	/**
	 * 获取最后一页页码，也就是总页数
	 *
	 * @return 最后一页页码
	 */
	public int getLastPageNumber() {
		return PageUtils.computeLastPageNumber(total, pageSize);
	}

	/**
	 * 总的数据条目数量，0表示没有数据
	 *
	 * @return 总数量
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * 获取当前页的首条数据的行编码
	 *
	 * @return 当前页的首条数据的行编码
	 */
	public int getThisPageFirstElementNumber() {
		return (getThisPageNumber() - 1) * getPageSize() + 1;
	}

	/**
	 * 获取当前页的末条数据的行编码
	 *
	 * @return 当前页的末条数据的行编码
	 */
	public int getThisPageLastElementNumber() {
		int fullPage = getThisPageFirstElementNumber() + getPageSize() - 1;
		return getTotal() < fullPage ? getTotal() : fullPage;
	}

	/**
	 * 获取下一页编码
	 *
	 * @return 下一页编码
	 */
	public int getNextPageNumber() {
		return getThisPageNumber() + 1;
	}

	/**
	 * 获取上一页编码
	 *
	 * @return 上一页编码
	 */
	public int getPreviousPageNumber() {
		return getThisPageNumber() - 1;
	}

	/**
	 * 每一页显示的条目数
	 *
	 * @return 每一页显示的条目数
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 当前页的页码
	 *
	 * @return 当前页的页码
	 */
	public int getThisPageNumber() {
		return pageNumber;
	}

	/**
	 * 得到用于多页跳转的页码
	 * @return
	 */
	public Integer[] getLinkPageNumbers() {
		return linkPageNumbers(7);
	}

	/**
	 * 得到用于多页跳转的页码
	 * 注意:不可以使用 getLinkPageNumbers(10)方法名称，因为在JSP中会与 getLinkPageNumbers()方法冲突，报exception
	 * @return
	 */
	public Integer[] linkPageNumbers(int count) {
		return PageUtils.generateLinkPageNumbers(getThisPageNumber(), getLastPageNumber(), count);
	}

	/**
	 * 得到数据库的第一条记录号
	 * @return
	 */
	public int getFirstResult() {
		return PageUtils.getFirstResult(pageNumber, pageSize);
	}

}
