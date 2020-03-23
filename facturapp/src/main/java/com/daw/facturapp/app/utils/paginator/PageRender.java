package com.daw.facturapp.app.utils.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {
	
	private String url;
	private Page<T> page;
	private int totalPages;
	private int elementsByPage;
	private int currentPage;
	private List<PageItem> pages;
	
	public PageRender(String url, Page<T> page) {
		super();
		this.url = url;
		this.page = page;
		this.pages = new ArrayList<PageItem>();
		this.elementsByPage = page.getSize();
		this.totalPages = page.getTotalPages();
		this.currentPage = page.getNumber() + 1;
		
		int since, until;
		if(totalPages <= elementsByPage) {
			since = 1;
			until = totalPages;
		} else {
			if(currentPage <= elementsByPage/2) {
				since = 1;
				until = elementsByPage;
			} else if(currentPage >= totalPages - elementsByPage/2) {
				since = totalPages - elementsByPage + 1;
				until = elementsByPage;
			} else {
				since = currentPage - elementsByPage/2;
				until = elementsByPage;
			}
		}
		
		for(int i = 0; i < until; i++) {
			this.pages.add(new PageItem(since + i, currentPage == since +i));
		}
	}
	
	public String getUrl() {
		return url;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public List<PageItem> getPages() {
		return pages;
	}
	
	public boolean isFirst() {
		return page.isFirst();
	}
	
	public boolean isLast() {
		return page.isLast();
	}
	
	public boolean isHasNext() {
		return page.hasNext();
	}
	
	public boolean isHasPrevious() {
		return page.hasPrevious();
	}

}
