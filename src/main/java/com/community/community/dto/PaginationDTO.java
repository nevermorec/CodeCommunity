package com.community.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class PaginationDTO {
	private List<QuestionDTO> questions;
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

	public List<QuestionDTO> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionDTO> questions) {
		this.questions = questions;
	}

	public boolean isShowPrevious() {
		return showPrevious;
	}

	public void setShowPrevious(boolean showPrevious) {
		this.showPrevious = showPrevious;
	}

	public boolean isShowFirstPage() {
		return showFirstPage;
	}

	public void setShowFirstPage(boolean showFirstPage) {
		this.showFirstPage = showFirstPage;
	}

	public boolean isShowNext() {
		return showNext;
	}

	public void setShowNext(boolean showNext) {
		this.showNext = showNext;
	}

	public boolean isShowEndPage() {
		return showEndPage;
	}

	public void setShowEndPage(boolean showEndPage) {
		this.showEndPage = showEndPage;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public List<Integer> getPages() {
		return pages;
	}

	public void setPages(List<Integer> pages) {
		this.pages = pages;
	}
}
