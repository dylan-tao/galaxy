package org.javaosc.galaxy.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */

public class Page<T> {
	
	private static final int defaultPageNo = 1;
	
	protected int pageNo;
	protected int pageSize; //suggest this set value in action
	protected long totalCount = 0;
	
	protected boolean autoCount = false;
	protected long lastQueryTime;
	
	protected List<T> result =  new ArrayList<T>();
	
	public Page() {}

	public Page(int pageSize, int pageNo) {
		this.pageNo = pageNo < 1 ? defaultPageNo : pageNo;
		this.pageSize = pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo < 1 ? defaultPageNo : pageNo;
	}
	
	public int getPageNo() {
		return pageNo;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}
	
	public List<T> getResult() {
		return result;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(final long totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalPage() {
		if (totalCount < 0) {
			return 0;
		}else{
			int count = (int)((totalCount + pageSize - 1) / pageSize);
			return count;
		}
	}

	public boolean isAutoCount() {
		return autoCount;
	}

	public void setAutoCount(boolean autoCount) {
		this.autoCount = autoCount;
	}

	public void setLastQueryTime(long lastQueryTime) {
		this.lastQueryTime = lastQueryTime;
	}
	
	public long getLastQueryTime() {
		return lastQueryTime;
	}
}
