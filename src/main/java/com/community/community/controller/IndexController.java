package com.community.community.controller;

import com.community.community.dto.PaginationDTO;
import com.community.community.mapper.UserMapper;
import com.community.community.service.NotificationService;
import com.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class IndexController {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private NotificationService notificationService;

	@GetMapping("/")
	public String greeting(Model model,
						   @RequestParam(name = "page", defaultValue = "1")Integer page,
						   @RequestParam(name = "size", defaultValue = "3")Integer size) {
		PaginationDTO pagination = questionService.list(page, size);
		model.addAttribute("pagination", pagination);

		return "index";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response){
		request.getSession().removeAttribute("user");
		Cookie cookie = new Cookie("token", null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		return "redirect:/";
	}
}
