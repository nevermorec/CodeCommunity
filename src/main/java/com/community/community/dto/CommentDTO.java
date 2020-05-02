package com.community.community.dto;

import com.community.community.model.Comment;
import com.community.community.model.User;
import lombok.Data;

public class CommentDTO extends Comment {
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
