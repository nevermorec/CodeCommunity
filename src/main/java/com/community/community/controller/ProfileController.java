package com.community.community.controller;

import com.community.community.dto.PaginationDTO;
import com.community.community.mapper.UserMapper;
import com.community.community.model.User;
import com.community.community.service.NotificationService;
import com.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/profile")
public class ProfileController {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private NotificationService notificationService;

	@GetMapping("/questions")
	public String profileQuestion(Model model, HttpServletRequest request,
						  @RequestParam(name = "page", defaultValue = "1")Integer page,
						  @RequestParam(name = "size", defaultValue = "3")Integer size) {
		User user = (User) request.getSession().getAttribute("user");

		model.addAttribute("sectionName", "我的问题");
		model.addAttribute("section", "questions");
		PaginationDTO pagination = questionService.listByUser(user.getId(), page, size);
		model.addAttribute("pagination", pagination);
		long unreadCount = notificationService.unreadCount(user.getId());
		model.addAttribute("unreadCount", unreadCount);
		return "profile";
	}

	@GetMapping("/replies")
	public String profileReplies(Model model, HttpServletRequest request,
								 @RequestParam(name = "page", defaultValue = "1")Integer page,
								 @RequestParam(name = "size", defaultValue = "3")Integer size) {
		User user = (User) request.getSession().getAttribute("user");

		model.addAttribute("sectionName", "最新回复");
		model.addAttribute("section", "replies");
		PaginationDTO pagination = notificationService.listByUserId(user.getId(), page, size);
		model.addAttribute("pagination", pagination);
		long unreadCount = notificationService.unreadCount(user.getId());
		model.addAttribute("unreadCount", unreadCount);
		return "profile";
	}
}
