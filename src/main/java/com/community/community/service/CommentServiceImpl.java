package com.community.community.service;

import com.community.community.dto.CommentDTO;
import com.community.community.exception.CustomizeErrorCode;
import com.community.community.exception.CustomizeException;
import com.community.community.mapper.CommentMapper;
import com.community.community.mapper.QuestionMapper;
import com.community.community.mapper.UserMapper;
import com.community.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentMapper commentMapper;
	@Autowired
	private QuestionMapper questionMapper;
	@Autowired
	private UserMapper userMapper;

	@Override
	@Transactional
	public void insertComment(Comment comment, User user) {
		if (comment.getParentId() == null || comment.getParentId() == 0) {
			throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
		}
		if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
			throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
		}

		if (comment.getType()==CommentTypeEnum.COMMENT.getType()) {
			// 回复评论
			Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
			if (dbComment==null) throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
			commentMapper.insertSelective(comment);
			dbComment.setCommentCount(1);
			commentMapper.increaseCommentCount(dbComment);
		} else {
			// 回复问题
			Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
			if (question==null) throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
			commentMapper.insertSelective(comment);
			question.setCommentCount(1);
			questionMapper.increaseCommentCount(question);
		}
	}

	@Override
	public List<CommentDTO> listByTargetId(Integer id, CommentTypeEnum typeEnum) {
		CommentExample commentExample = new CommentExample();
		commentExample.createCriteria()
				.andParentIdEqualTo(id)
				.andTypeEqualTo(typeEnum.getType());
		commentExample.setOrderByClause("GMT_CREATE DESC");
		List<Comment> comments = commentMapper.selectByExample(commentExample);
		if(comments.size()==0) return new ArrayList<>();
		Set<Integer> commentatorsSet = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
		List<Integer> commentators = new ArrayList<>();
		commentators.addAll(commentatorsSet);

		UserExample userExample = new UserExample();
		userExample.createCriteria().andIdIn(commentators);
		List<User> users = userMapper.selectByExample(userExample);
		Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
		List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
			CommentDTO commentDTO = new CommentDTO();
			BeanUtils.copyProperties(comment, commentDTO);
			commentDTO.setUser(userMap.get(comment.getCommentator()));
			return commentDTO;
		}).collect(Collectors.toList());
		return commentDTOS;
	}
}
