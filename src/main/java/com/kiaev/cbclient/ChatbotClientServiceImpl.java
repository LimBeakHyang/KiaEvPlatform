package com.kiaev.cbclient;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender; // 1. 여기도 잠시 주석
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatbotClientServiceImpl implements ChatbotClientService {

    private final ChatbotClientRepository repository;
    
    // 2. 이 부분을 주석 처리해야 스프링이 JavaMailSender 부품을 찾지 않습니다.
    // private final JavaMailSender mailSender; 

    @Override
    @Transactional
    public void registerInquiry(ChatInquiry chatInquiry) {
        // 1. DB 저장 확인용
        repository.save(chatInquiry);
        
        // 2. 이메일 알림 발송 (잠시 꺼둠)
        // sendMail(chatInquiry);
    }

    private void sendMail(ChatInquiry chatInquiry) {
        // mailSender를 주석 처리했으므로 이 안의 로직도 잠시 비워둡니다.
        /*
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("admin@kiaev.com");
        message.setSubject("[KiaEV] 신규 문의 알림");
        message.setText("내용: " + chatInquiry.getContent());
        */
    }
}