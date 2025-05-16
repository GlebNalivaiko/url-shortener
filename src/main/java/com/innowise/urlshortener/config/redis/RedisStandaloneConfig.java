package com.innowise.urlshortener.config.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class RedisStandaloneConfig {

  private static final String REDIS_HOST_TEMPLATE = "redis://%s:%d";

  @Bean(destroyMethod = "shutdown")
  public RedissonClient redisson(RedisProperties redisProperties) {
    var config = new Config();
    config.useSingleServer()
        .setAddress(REDIS_HOST_TEMPLATE.formatted(redisProperties.getHost(), redisProperties.getPort()))
        .setPassword(redisProperties.getPassword());
    config.setCodec(StringCodec.INSTANCE);

    return Redisson.create(config);
  }
}
