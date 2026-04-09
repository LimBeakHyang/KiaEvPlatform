package com.kiaev.cbclient;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chatbot")
@RequiredArgsConstructor
public class ChatbotClientRestController {

    private final ChatbotClientService chatbotClientService;

    @PostMapping("/inquiry")
    public ResponseEntity<String> saveInquiry(@RequestBody ChatInquiry chatInquiry) {
        try {
            chatbotClientService.registerInquiry(chatInquiry);
            return ResponseEntity.ok("문의가 성공적으로 접수되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("오류 발생: " + e.getMessage());
        }
    }
}