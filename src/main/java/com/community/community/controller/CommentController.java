package com.community.community.controller;

import com.community.community.dto.CommentCreateDTO;
import com.community.community.dto.CommentDTO;
import com.community.community.dto.ResultDTO;
import com.community.community.exception.CustomizeErrorCode;
import com.community.community.mapper.CommentMapper;
import com.community.community.mapper.UserMapper;
import com.community.community.model.Comment;
import com.community.community.model.CommentTypeEnum;
import com.community.community.model.User;
import com.community.community.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {
	@Autowired
	private CommentMapper commentMapper;
	@Autowired
	private CommentService commentService;
	@Autowired
	private UserMapper userMapper;

	@ResponseBody
	@PostMapping("/comment")
	public Object comment(@RequestBody CommentCreateDTO commentCreateDTO,
						  HttpServletRequest request)  {
		User user = (User) request.getSession().getAttribute("user");
		if (user==null) return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);

		if (commentCreateDTO==null || StringUtils.isEmpty(commentCreateDTO)) {
			return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
		}

		Comment comment = new Comment();
		BeanUtils.copyProperties(commentCreateDTO, comment);
		comment.setCommentator(user.getId());
		comment.setGmtCreate(System.currentTimeMillis());
		comment.setGmtModified(System.currentTimeMillis());

		commentService.insertComment(comment, user);
		return ResultDTO.okOf();
	}

	@ResponseBody
	@GetMapping("/comment/{id}")
	public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Integer id)  {
		List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
		return ResultDTO.okOf(commentDTOS);
	}
}
