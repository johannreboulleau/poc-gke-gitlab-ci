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
        log.info("createJedisPool");

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // Default : 8, consider how many concurrent connections into Redis you will need under load
        poolConfig.setMaxTotal(128);
        // Number of connections to Redis that just sit there and do nothing
        poolConfig.setMaxIdle(16);
        // Minimum number of idle connections to Redis - these can be seen as always open and ready to serve
        poolConfig.setMinIdle(8);

        // Tests whether connection is dead when returning a connection to the pool
        poolConfig.setTestOnBorrow(true);
        // Tests whether connection is dead when connection retrieval method is called
        poolConfig.setTestOnReturn(true);
        // Tests whether connections are dead during idle periods
        poolConfig.setTestWhileIdle(true);

        return new JedisPool(poolConfig, redisProperties.getHost(), redisProperties.getPort());
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        JedisPool jedisPool = (JedisPool) event.getServletContext().getAttribute("jedisPool");
        if (jedisPool != null) {
            jedisPool.destroy();
            event.getServletContext().setAttribute("jedisPool", null);
            log.info("contextDestroyed");
        }
    }

    // Run this before web application is started
    @Override
    public void contextInitialized(ServletContextEvent event) {

        if (this.jedisPool == null) {
            this.jedisPool = createJedisPool();
            log.info("contextInitialized");
        }
    }

    public JedisPool getJedisPool() {

        if (this.jedisPool == null) {
            this.jedisPool = createJedisPool();
        }

        log.info("getJedisPool = {}", this.jedisPool);

        return this.jedisPool;
    }

}
