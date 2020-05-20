package com.community.community.controller;

import com.community.community.Provider.RedisProvide;
import com.community.community.dto.CommentDTO;
import com.community.community.dto.QuestionDTO;
import com.community.community.model.CommentTypeEnum;
import com.community.community.service.CommentService;
import com.community.community.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@Slf4j
public class QuestionController {
	@Value("${redis.question.table}")
	private String questionDTORedisTable;
	@Value("${redis.hottopic.zset}")
	private String hotTopicRedisList;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RedisProvide redisProvide;

	@GetMapping("/question/{id}")
	public String questionById(@PathVariable(name = "id") Integer id,
							   Model model){
		QuestionDTO questionDTO = redisProvide.findFromReidsById(id);


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
