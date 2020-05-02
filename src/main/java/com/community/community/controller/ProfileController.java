package com.community.community.controller;

import com.community.community.dto.PaginationDTO;
import com.community.community.mapper.UserMapper;
import com.community.community.model.User;
import com.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/profile")
public class ProfileController {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private QuestionService questionService;

	@GetMapping("/questions")
	public String profile(Model model, HttpServletRequest request,
						  @RequestParam(name = "page", defaultValue = "1")Integer page,
						  @RequestParam(name = "size", defaultValue = "3")Integer size) {
		User user = (User) request.getSession().getAttribute("user");

		model.addAttribute("sectionName", "我的问题");
		model.addAttribute("section", "questions");
		PaginationDTO pagination = questionService.listByUser(user.getId(), page, size);
		model.addAttribute("pagination", pagination);
		return "profile";
	}
}
