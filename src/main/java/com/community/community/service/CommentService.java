package com.community.community.service;

import com.community.community.dto.CommentDTO;
import com.community.community.model.Comment;
import com.community.community.model.CommentTypeEnum;
import com.community.community.model.User;

import java.util.List;

public interface CommentService {
	void insertComment(Comment comment, User user);

	List<CommentDTO> listByTargetId(Integer id, CommentTypeEnum typeEnum);
}
