package com.ymj.spring.redis.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author : yemingjie
 * @date : 2021/5/9 18:56
 */

@Component
public class TestRedis {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    @Qualifier("getMyRedisTemplate")
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public void test() {
        redisTemplate.opsForValue().set("hello", "china");
        System.out.println(redisTemplate.opsForValue().get("hello"));
    }

    public void test2() {
        stringRedisTemplate.opsForValue().set("hello01", "china");
        System.out.println(stringRedisTemplate.opsForValue().get("hello01"));
    }

    public void test3() {
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        connection.set("hello03".getBytes(), "yemingjie".getBytes());
        System.out.println(new String(connection.get("hello03".getBytes())));
    }

    public void testHash() {
        HashOperations<String, Object, Object> hash = stringRedisTemplate.opsForHash();
        hash.put("sean", "name", "yemingjie");
        hash.put("sean", "age", "24");
        System.out.println(hash.entries("sean"));
    }

    public void testHash2() {
        Person person = new Person();
        person.setName("zhangsan");
        person.setAge(16);

        // 使用stringRedisTemplate就不会在前面出现乱码
        // 添加这一段就能防止类型报错，给值设置序列化器
        stringRedisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));

        Jackson2HashMapper jm = new Jackson2HashMapper(objectMapper, false);
        // 把对象变为键值对
        stringRedisTemplate.opsForHash().putAll("sean01", jm.toHash(person));

        Map map = stringRedisTemplate.opsForHash().entries("sean01");
        Person p = objectMapper.convertValue(map, Person.class);
        System.out.println(p.getName());
    }


    /**
     * 使用新的stringRedisTemplate
     */
    public void testHash3() {
        Person person = new Person();
        person.setName("zhangsan");
        person.setAge(16);

        Jackson2HashMapper jm = new Jackson2HashMapper(objectMapper, false);
        // 把对象变为键值对
        stringRedisTemplate.opsForHash().putAll("sean01", jm.toHash(person));

        Map map = stringRedisTemplate.opsForHash().entries("sean01");
        Person p = objectMapper.convertValue(map, Person.class);
        System.out.println(p.getName());
    }
}
