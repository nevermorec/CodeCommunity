package com.community.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> {
	private List<T> data;
	private boolean showPrevious;
	private boolean showFirstPage;
	private boolean showNext;
	private boolean showEndPage;
	private Integer page;
	private Integer totalPage;

	private List<Integer> pages = new ArrayList<>();

	public void setPagination(Integer totalCount, Integer page, Integer size) {
		this.page = page;
		Integer totalPage = (int)Math.ceil(totalCount / (double)size);
		this.totalPage = totalPage;

		int firstPage = (page>3)?(page-2):1;
		int endPage = (page<(totalPage-2))?(page+2):totalPage;
		for (int i = firstPage; i <= endPage; i++) {
			pages.add(i);
		}

		showPrevious = page != 1;
		showNext = !page.equals(totalPage);
		showFirstPage = !pages.contains(1);
		showEndPage = !pages.contains(totalCount);
	}
}
