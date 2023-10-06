package com.example.demoone;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class DemoOneApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    void setRedisTest() {
        redisTemplate.boundValueOps("name").set("zhangsan");
    }

    @Test
    public void getRedisTest() {
        Object name = redisTemplate.boundValueOps("name").get();
        Object surname = redisTemplate.boundValueOps("surname").get();
        System.out.println(name+" "+surname);
    }

}
