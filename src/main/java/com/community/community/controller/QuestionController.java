package com.community.community.controller;

import com.community.community.dto.CommentDTO;
import com.community.community.dto.QuestionDTO;
import com.community.community.model.CommentTypeEnum;
import com.community.community.service.CommentService;
import com.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
	@Autowired
	private QuestionService questionService;

	@Autowired
	private CommentService commentService;

	@GetMapping("/question/{id}")
	public String questionById(@PathVariable(name = "id") Integer id,
							   Model model){

		QuestionDTO questionDTO = questionService.getById(id);

		// 显示相关问题
		List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
		model.addAttribute("relatedQuestions", relatedQuestions);


		questionService.increaseViewCount(id);
		model.addAttribute("questionDTO", questionDTO);

		// 显示评论
		List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
		model.addAttribute("commentDTOs", commentDTOS);
		return "question";
		

		
	}
}
