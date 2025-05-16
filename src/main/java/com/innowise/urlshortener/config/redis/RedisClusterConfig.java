package com.innowise.urlshortener.config.redis;

import com.innowise.urlshortener.config.properties.RedisClusterConnectionProperties;
import java.util.Arrays;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.FailedConnectionDetector;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!local")
public class RedisClusterConfig {

  @Bean(destroyMethod = "shutdown")
  public RedissonClient redisson(RedisClusterConnectionProperties redisConnectionProperties) {
    var config = new Config();
    config.useClusterServers()
        .setScanInterval(redisConnectionProperties.getScanInterval())
        .setIdleConnectionTimeout(redisConnectionProperties.getIdleConnectionTimeout())
        .setConnectTimeout(redisConnectionProperties.getConnectTimeout())
        .setTimeout(redisConnectionProperties.getTimeout())
        .setRetryAttempts(redisConnectionProperties.getRetryAttempts())
        .setRetryInterval(redisConnectionProperties.getRetryInterval())
        .setFailedSlaveReconnectionInterval(redisConnectionProperties.getFailedSlaveReconnectionInterval())
        .setFailedSlaveNodeDetector(
            new FailedConnectionDetector(redisConnectionProperties.getFailedSlaveCheckInterval()))
        .setSubscriptionsPerConnection(redisConnectionProperties.getSubscriptionsPerConnection())
        .setSubscriptionConnectionMinimumIdleSize(redisConnectionProperties.getSubscriptionConnectionMinimumIdleSize())
        .setSubscriptionConnectionPoolSize(redisConnectionProperties.getSubscriptionConnectionPoolSize())
        .setMasterConnectionMinimumIdleSize(redisConnectionProperties.getMasterConnectionMinimumIdleSize())
        .setMasterConnectionPoolSize(redisConnectionProperties.getMasterConnectionPoolSize())
        .setReadMode(redisConnectionProperties.getReadMode())
        .setSubscriptionMode(redisConnectionProperties.getSubscriptionMode())
        .setPingConnectionInterval(redisConnectionProperties.getPingConnectionInterval())
        .setKeepAlive(redisConnectionProperties.getKeepAlive())
        .setTcpNoDelay(redisConnectionProperties.getTcpNoDelay())
        .addNodeAddress(Arrays.stream(redisConnectionProperties.getNodes())
            .map(node -> "redis://" + node)
            .toArray(String[]::new));
    config.setCodec(StringCodec.INSTANCE);

    return Redisson.create(config);
  }
}
