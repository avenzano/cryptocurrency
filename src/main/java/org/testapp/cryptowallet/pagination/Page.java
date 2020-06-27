package org.testapp.cryptowallet.pagination;

import java.util.Collections;
import java.util.List;

public class Page<T> {

	private List<T> data;
	
	private Integer total;
	
	private boolean prev;
	
	private boolean next;
	
	public Page() {		
	}
	
	public Page(List<T> pageData, int total, int offset, int pageSize) {
		this.data = pageData;
		this.total = total;
		this.prev = offset > 0;
		this.next = offset + pageSize < total;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public boolean isPrev() {
		return prev;
	}

	public void setPrev(boolean prev) {
		this.prev = prev;
	}

	public boolean isNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public static final Page EMPTY_PAGE = new Page<>(Collections.EMPTY_LIST, 0, 0, 0);
	
    @SuppressWarnings("unchecked")
    public static final <T> Page<T> emptyPage() {
        return (Page<T>) EMPTY_PAGE;
    }
    
}
