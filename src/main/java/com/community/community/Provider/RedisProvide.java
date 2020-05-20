package com.community.community.Provider;

import com.alibaba.fastjson.JSON;
import com.community.community.dto.QuestionDTO;
import com.community.community.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class RedisProvide {
    @Value("${redis.question.table}")
    private String questionDTORedisTable;

    @Value("${redis.hottopic.zset}")
    private String hotTopicRedisList;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private QuestionService questionService;

    public List<QuestionDTO> hotTopicQuestion() {
        List<QuestionDTO> hotTopic = new ArrayList<>();
        Set<String> questionList = stringRedisTemplate.opsForZSet().reverseRange(hotTopicRedisList, 0L, 4L);
        for (String questionId : questionList) {
            log.info(questionId);
            Integer id = Integer.parseInt(questionId);
            hotTopic.add(findFromReidsById(id));
        }
        return hotTopic;
    }

    public QuestionDTO findFromReidsById(Integer id) {
        QuestionDTO questionDTO;
        if (stringRedisTemplate.hasKey(questionDTORedisTable +id)) {
            // 从Redis中取出questionDTO
            questionDTO = JSON.parseObject(stringRedisTemplate.opsForValue().get(questionDTORedisTable +id), QuestionDTO.class);
            log.info("从Reids取出数据");
        } else {
            questionDTO = questionService.getById(id);
            // 把questionDTO存入Redis
            String quesitonDTOJSON = JSON.toJSONString(questionDTO);
            stringRedisTemplate.opsForValue().set(questionDTORedisTable +id, quesitonDTOJSON);
            stringRedisTemplate.opsForZSet().add(hotTopicRedisList, id.toString(), questionDTO.getViewCount());
            log.info("从Mysql取出数据并存入Redis");
        }
        increaseViewCount(id);
        return questionDTO;
    }

    public void increaseViewCount(Integer id) {
        stringRedisTemplate.opsForZSet().incrementScore(hotTopicRedisList, id.toString(), 1);
    }
}
