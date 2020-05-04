package com.community.community.service;

import com.community.community.dto.PaginationDTO;
import com.community.community.dto.QuestionDTO;
import com.community.community.dto.QuestionQueryDTO;
import com.community.community.exception.CustomizeErrorCode;
import com.community.community.exception.CustomizeException;
import com.community.community.mapper.QuestionMapper;
import com.community.community.mapper.UserMapper;
import com.community.community.model.Question;
import com.community.community.model.QuestionExample;
import com.community.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private QuestionMapper questionMapper;

	@Override
	public PaginationDTO<QuestionDTO> list(String search, Integer page, Integer size) {
		if (!StringUtils.isEmpty(search)) {
			String[] tags = search.split(" ");
			search = Arrays.stream(tags).collect(Collectors.joining("|"));
		}

		PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO();
		QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
		questionQueryDTO.setSearch(search);
		Integer totalCount = questionMapper.countBySearch(questionQueryDTO);
		Integer totalPage = (int)Math.ceil(totalCount/(double)size);

		if (page<1) page = 1;

		if(page>totalPage) page = totalPage;

		int offset = (page-1)*size;

		List<QuestionDTO> questionDTOlist = new ArrayList<>();
//		QuestionExample example = new QuestionExample();
//		example.setOrderByClause("gmt_create desc");
//		List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example, new RowBounds(offset, size));
		questionQueryDTO.setSize(size);
		questionQueryDTO.setPage(offset);
		List<Question> questions = questionMapper.selectBySearch(questionQueryDTO);

		for (Question question : questions) {
			User user = userMapper.selectByPrimaryKey(question.getCreator());
//			QuestionDTO questionDTO = new QuestionDTO(question);
			QuestionDTO questionDTO = new QuestionDTO();
			BeanUtils.copyProperties(question, questionDTO);
			questionDTO.setUser(user);
			questionDTOlist.add(questionDTO);
		}

		paginationDTO.setData(questionDTOlist);
		paginationDTO.setPagination(totalCount, page, size);
		return paginationDTO;
	}

	@Override
	public PaginationDTO<QuestionDTO> listByUser(Integer userId, Integer page, Integer size) {
		PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO();
		QuestionExample example = new QuestionExample();
		example.createCriteria().andCreatorEqualTo(userId);
		Integer totalCount = (int)questionMapper.countByExample(example);
		Integer totalPage = (int)Math.ceil(totalCount/(double)size);

		if (page<1) page = 1;
		if (page>totalPage) page = totalPage;

		int offset = (page-1)*size;

		List<QuestionDTO> questionDTOlist = new ArrayList<>();
		List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));


		for (Question question : questions) {
			User user = userMapper.selectByPrimaryKey(question.getCreator());
			QuestionDTO questionDTO = new QuestionDTO(question);
			questionDTO.setUser(user);
			questionDTOlist.add(questionDTO);
		}

		paginationDTO.setData(questionDTOlist);
		paginationDTO.setPagination(totalCount, page, size);
		return paginationDTO;
	}

	@Override
	public QuestionDTO getById(Integer id) {
		Question question = questionMapper.selectByPrimaryKey(id);
		if (question==null) {
			throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
		}
		User user = userMapper.selectByPrimaryKey(question.getCreator());
		QuestionDTO questionDTO = new QuestionDTO(question);
		questionDTO.setUser(user);
		return questionDTO;
	}

	@Override
	public void updateOrCreateQuestion(Question question) {
		if (question.getId()!=null) {
			// 更新问题
			int i = questionMapper.updateByPrimaryKeySelective(question);
			if (i!=1) {
				throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
			}
		} else {
			// 增加问题
			questionMapper.insertSelective(question);
		}
	}



	@Override
	public void increaseViewCount(Integer id) {
//		Question question = questionMapper.selectByPrimaryKey(id);
//		question.setViewCount(question.getViewCount()+1);
//		questionMapper.updateByPrimaryKeySelective(question);
		Question question = new Question();
		question.setId(id);
		question.setViewCount(1);
		questionMapper.increaseViewCount(question);
	}

	@Override
	public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
		if (StringUtils.isEmpty(queryDTO.getTag())) {
			return new ArrayList<>();
		}
		String[] tags = queryDTO.getTag().split(",");
		String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
		Question question = new Question();
		question.setId(queryDTO.getId());
		question.setTag(regexpTag);

		List<Question> questions = questionMapper.selectRelated(question);
		List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
			QuestionDTO questionDTO = new QuestionDTO();
			BeanUtils.copyProperties(q, questionDTO);
			return questionDTO;
		}).collect(Collectors.toList());
		return questionDTOS;
	}



}
