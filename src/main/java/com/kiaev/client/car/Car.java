package com.kiaev.client.car;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CAR_TBL")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carNo;             // 차량 고유 번호 (PK)

    @Column(nullable = false, unique = true, length = 100)
    private String modelName;       // 모델명 (예: EV6, EV9)

    @Column(nullable = false, length = 30)
    private String carType;         // 차종 (예: SUV, 세단)

    @Column(nullable = false, length = 255)
    private String carColor;        // 대표 색상

    @Column(nullable = false)
    private Long price;             // 판매 가격

    @Column(nullable = false, length = 50)
    private String batteryCapacity; // 배터리 용량 (예: 77.4kWh)

    @Column(nullable = false)
    private Integer drivingRangeKm; // 1회 충전 주행 가능 거리 (km)

    @Column(length = 50)
    private String fastChargeTime;  // 급속 충전 시간 정보

    @Column(length = 100)
    private String chargeInfo;      // 충전 관련 추가 정보

    @Column(length = 1000)
    private String carDescription;  // 차량 상세 설명

    @Column(length = 255)
    private String imagePath;       // 차량 이미지 파일 경로

    @Column(nullable = false, length = 20)
    private String saleStatus;      // 판매 상태 (판매중, 품절 등)

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 등록 일시

    private LocalDateTime updatedAt; // 수정 일시

    // 데이터 저장 전 실행되는 로직
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.saleStatus == null) this.saleStatus = "판매중"; // 기본값 설정
    }
}