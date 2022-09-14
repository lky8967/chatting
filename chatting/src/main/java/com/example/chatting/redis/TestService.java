package com.example.chatting.redis;

import org.springframework.stereotype.Service;

@Service
public class TestService {


    public TestDto getTestSvc(String id){
        TestDto tvo = new TestDto();
        tvo.setId(id);
        tvo.setText( id + "님, 안녕하세요~!");

        System.out.println("[id:" + id + "] Service 에서 연산을 수행합니다");

        return tvo;
    }
}
