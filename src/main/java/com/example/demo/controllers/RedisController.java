package com.example.demo.controllers;

import com.example.demo.servlets.RedisServletContextListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletResponse;
import java.net.SocketException;

@RestController
@RequestMapping("redis")
@Slf4j
public class RedisController {

    @Autowired
    private RedisServletContextListener redisServletContextListener;

    @GetMapping
    public String getVisitCounter() throws SocketException {
            JedisPool jedisPool = this.redisServletContextListener.getJedisPool();

            if (jedisPool == null) {
                throw new SocketException("Error connecting to Jedis pool");
            }
            Long visits;

            try (Jedis jedis = jedisPool.getResource()) {
                visits = jedis.incr("visits");
            }

            return "Visitor counter: " + visits;
    }

}
