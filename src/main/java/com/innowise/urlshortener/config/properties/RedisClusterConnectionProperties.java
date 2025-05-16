package com.innowise.urlshortener.config.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.redisson.config.ReadMode;
import org.redisson.config.SubscriptionMode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

@Getter
@Profile("!local")
@RequiredArgsConstructor
@ConfigurationProperties("spring.redis.cluster")
public class RedisClusterConnectionProperties {

  private final String[] nodes;
  private final Integer scanInterval;
  private final Integer idleConnectionTimeout;
  private final Integer connectTimeout;
  private final Integer timeout;
  private final Integer retryAttempts;
  private final Integer retryInterval;
  private final Integer failedSlaveReconnectionInterval;
  private final Integer failedSlaveCheckInterval;
  private final Integer subscriptionsPerConnection;
  private final Integer subscriptionConnectionMinimumIdleSize;
  private final Integer subscriptionConnectionPoolSize;
  private final Integer slaveConnectionMinimumIdleSize;
  private final Integer slaveConnectionPoolSize;
  private final Integer masterConnectionMinimumIdleSize;
  private final Integer masterConnectionPoolSize;
  private final ReadMode readMode;
  private final SubscriptionMode subscriptionMode;
  private final Integer pingConnectionInterval;
  private final Boolean keepAlive;
  private final Boolean tcpNoDelay;
}
