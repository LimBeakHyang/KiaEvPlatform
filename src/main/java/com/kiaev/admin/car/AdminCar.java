package com.kiaev.admin.car;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "car")
public class AdminCar {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;

    // 차량번호
    @Column(nullable = false, unique = true)
    private String carNumber;

    // 모델명
    @Column(nullable = false)
    private String carName;

    private String carBrand;              // 브랜드
    private String carType;               // 차량 유형
    private String carColor;              // 차량 색상

    private Integer carPrice;             // 가격(숫자)
    private String carPriceDisplay;       // 가격 표시용

    private String batteryCapacity;       // 배터리 용량
    private String drivingRange;          // 주행거리
    private String fastChargeTime;        // 급속충전 시간
    private String chargePercentInfo;     // 충전 퍼센트

    @Column(length = 3000)
    private String carContent;            // 차량 설명

    private String carStatus = "판매중";   // 기본값

    // 이미지
    private String carMainImg;
    private String carImg1;
    private String carImg2;
    private String carImg3;

    // 생성/수정 시간 (선택)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
