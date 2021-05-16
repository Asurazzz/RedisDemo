package com.ymj.spring.redis.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * @author : yemingjie
 * @date : 2021/5/9 20:08
 */
@Configuration
public class MyTemplate {

    @Bean
    public StringRedisTemplate getMyRedisTemplate(RedisConnectionFactory fc) {
        StringRedisTemplate srt = new StringRedisTemplate(fc);
        srt.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        return srt;
    }
}
