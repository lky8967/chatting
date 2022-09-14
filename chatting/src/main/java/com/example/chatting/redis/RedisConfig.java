package com.example.chatting.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
@Configuration
// 스프링 앱이 캐싱 기능을 사용할 수 있게 @EnablaeCaching 애노테이션을 추가해준다.
// spring boot 2.0 이상부터는 jedis 가 아닌 lettuce를 이용해서 redis 에 접속하는게 디폴트이다.
// jedis, lettuce 모두 redis 접속 connection pool 관리 라이브러리이며, lettuce 가 성능이 더 좋기에 디폴트로 세팅되어 있다.
// 참고한 블로그 https://loosie.tistory.com/807
@EnableCaching

public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }


    // 참고 블로그 https://kim-oriental.tistory.com/28
    @SuppressWarnings("deprecation")
    // 어노테이션 참고 블로그 https://ddolcat.tistory.com/393
    // deprecation 권장되지 않는 기능과 관련된 경고를 억제
    @Bean
    public CacheManager cacheManager() {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory());
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())) // Value Serializer 변경
                .prefixKeysWith("Test:") // Key Prefix로 "Test:"를 앞에 붙여 저장
                .entryTtl(Duration.ofMinutes(30)); // 캐시 수명 30분
        builder.cacheDefaults(configuration);
        return builder.build();
    }
}
