package com.kiaev.admin.consult;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "consult")
public class AdminConsult {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long consultId;
	
	// 상담번호
	@Column(nullable = false, unique = true)
	private String consultNumber;
	
	// 회원번호
	@Column(nullable = false)
	private String memberNumber;
	
	// 회원명
	@Column(nullable = false)
	private String memberName;
	
	// 차량명
	@Column(nullable = false)
	private String carName;
	
	// 상담상태
	@Column(nullable = false)
	private String consultStatus;
	
	// 예산
	private Integer consultBudget;
	
	// 사용목적
	private String consultPurpose;
	
	// 주행거리
	private String driveDistance;
	
	// 동승자 정보
	private String companionInfo;
	
	// 상담내용
	@Column(columnDefinition = "TEXT")
	private String consultContent;
	
	// 관리자 메모
	@Column(columnDefinition = "TEXT")
	private String adminMemo;
	
	// 배정 딜러 번호
	private String dealerNumber;
	
	// 배정 딜러명
	private String dealerName;
	
	// 신청일시
	@Column(nullable = false)
	private LocalDateTime createdAt;
	
	// 수정일시
	private LocalDateTime updatedAt;
}
