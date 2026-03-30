package com.kiaev.client.promotion;

import jakarta.persistence.*; // JPA 관련 라이브러리 (최신 버전 기준)
import lombok.*;             // Getter, Setter, 빌더 등 자동 생성
import java.time.LocalDateTime;

@Entity
@Table(name = "PROMOTION_TBL") // 실제 DB에 생성될 테이블 이름
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자 (무분별한 객체 생성 방지)
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자
@Builder // 빌더 패턴으로 객체 생성을 직관적으로 가능하게 함
public class Promotion {

    // 1. 프로모션 고유 ID (PK, 자동 증가 전략)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_id")
    private Long id;

    // 2. 프로모션 제목
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    // 3. 프로모션 상세 내용 (긴 글 처리를 위해 TEXT 타입 지정)
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    // 4. 할인 금액 (할인 정보 표시용)
    @Column(name = "discount_amount")
    private Integer discountAmount;

    // 5. 프로모션 시작 일시
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    // 6. 프로모션 종료 일시
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;
    
    // 7. 관리자 요구사항 반영: 활성화(노출) 여부
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true; // 기본값은 활성화(true)

    /**
     * 추가 비즈니스 로직: 현재 해당 프로모션이 진행 중인지 체크
     * @return 진행 중이면 true, 아니면 false
     */
    public boolean isCurrentlyActive() {
        LocalDateTime now = LocalDateTime.now();
        // 현재 시간이 시작 시간 이후이고, 종료 시간 이전인지 확인
        return (now.isAfter(this.startDate) || now.isEqual(this.startDate)) 
                && (now.isBefore(this.endDate) || now.isEqual(this.endDate));
    }
}