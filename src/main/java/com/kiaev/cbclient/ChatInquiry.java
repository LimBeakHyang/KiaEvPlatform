package com.kiaev.cbclient;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

// 반드시 jakarta.persistence 패키지를 사용해야 합니다 (Spring Boot 3 기준)
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CHAT_INQUIRY_TBL")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiry_no;       // 문의번호 (PK)

    private Long member_no;        // 회원번호 (FK)
    private Long car_no;           // 차량번호 (FK)

    @Column(nullable = false, length = 50)
    private String category;       // 문의유형

    @Column(nullable = false, length = 50)
    private String writer_name;    // 작성자명

    @Column(nullable = false, length = 100)
    private String writer_email;   // 작성자이메일

    @Column(nullable = false, length = 2000)
    private String content;        // 문의내용

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime created_at; // 등록일시

    @Builder.Default
    @Column(length = 1)
    private String is_answered = "N"; // 답변여부

    private Long admin_no;         // 담당관리자
}