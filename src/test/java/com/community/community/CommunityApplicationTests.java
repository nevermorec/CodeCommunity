package com.community.community;

import com.community.community.Provider.RedisProvide;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class CommunityApplicationTests {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RedisProvide redisProvide;

	@Test
	void contextLoads() {
	}

	@Test
	void t1() {
		redisProvide.hotTopicQuestion();
	}

}
