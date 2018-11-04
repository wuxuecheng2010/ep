package com.enze.ep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private RedisTemplate redisTemplate;

	@GetMapping("/test/{info}")
	public String test(@PathVariable(name = "info") String info) {

		return "x你好"+info;
	}

	@GetMapping("/get/{key}")
	public String getRedis(@PathVariable(name = "key") String key) {

		return stringRedisTemplate.opsForValue().get(key);
	}

	@GetMapping("/set/{key}/{value}")
	public String getRedis(@PathVariable(name = "key") String key, @PathVariable(name = "value") String value) {
		stringRedisTemplate.opsForValue().set(key, value);
		return "SUCCESS";
	}

	/*@GetMapping("/postEntity")
	public String postEntity() {
		User user = new User();
		user.setId(1);
		user.setName("pwl");
		user.setAge("25");
		redisTemplate.opsForValue().set(user.getId(), user);
		return "SUCCESS";
	}*/

	@GetMapping("/getEntity/{key}")
	public Object getEntity(@PathVariable(name = "key") String key) {
		return redisTemplate.opsForValue().get(key);
	}

}
