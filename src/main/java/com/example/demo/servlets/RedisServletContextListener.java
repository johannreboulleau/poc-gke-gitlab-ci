package com.example.demo.servlets;

import com.example.demo.properties.RedisProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.Properties;

@Slf4j
@Component
public class RedisServletContextListener implements ServletContextListener {

    @Autowired
    private RedisProperties redisProperties;

    private JedisPool jedisPool;

    private JedisPool createJedisPool() {

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // Default : 8, consider how many concurrent connections into Redis you will need under load
        poolConfig.setMaxTotal(128);

        return new JedisPool(poolConfig, redisProperties.getHost(), redisProperties.getPort());
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        JedisPool jedisPool = (JedisPool) event.getServletContext().getAttribute("jedisPool");
        if (jedisPool != null) {
            jedisPool.destroy();
            event.getServletContext().setAttribute("jedisPool", null);
        }
    }

    // Run this before web application is started
    @Override
    public void contextInitialized(ServletContextEvent event) {

        if (this.jedisPool == null) {
            this.jedisPool = createJedisPool();
        }
    }

    public JedisPool getJedisPool() {

        if (this.jedisPool == null) {
            this.jedisPool = createJedisPool();
        }

        return this.jedisPool;
    }

}
