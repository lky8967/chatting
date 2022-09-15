package com.example.chatting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.util.TimeZone;


@EnableCaching
@EnableJpaAuditing // DateTimeProvider 인터페이스를 상속하여 커스터마이징한다고 나와있습니다
@SpringBootApplication(exclude = {RedisRepositoriesAutoConfiguration.class})
public class ChattingApplication {

    //한국시간
    @PostConstruct
    public void timeZone() {
        // timezone Asia/Seoul 셋팅
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
    public static void main(String[] args) {
        SpringApplication.run(ChattingApplication.class, args);
    }

}
