package com.community.community.advice;

import com.community.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class CustomizeExceptionHandle {
	@ExceptionHandler(Exception.class)
	ModelAndView handle(Throwable e, Model model) {
		if (e instanceof CustomizeException) {
			model.addAttribute("msg", e.getMessage());
		} else {
			model.addAttribute("msg", "服务冒烟了，要不然你稍后再试试！！！");
		}
		return new ModelAndView("error");
	}
}
