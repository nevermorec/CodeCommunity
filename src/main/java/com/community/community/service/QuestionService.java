package com.community.community.service;

import com.community.community.dto.PaginationDTO;
import com.community.community.dto.QuestionDTO;
import com.community.community.model.Question;

import java.util.List;

public interface QuestionService {
	public PaginationDTO list(Integer page, Integer size);

	public Integer getTotalPage(Integer size);

	public PaginationDTO listByUser(Integer id, Integer page, Integer size);

	public QuestionDTO getById(Integer id);

	void updateOrCreateQuestion(Question question);

	public void increaseViewCount(Integer id);

	public List<QuestionDTO> selectRelated(QuestionDTO queryDTO);
}
