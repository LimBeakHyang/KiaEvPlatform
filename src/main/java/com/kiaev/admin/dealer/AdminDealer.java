package com.kiaev.admin.dealer;

import java.time.LocalDate;
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
@Table(name = "dealer")
public class AdminDealer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long dealerId;
	
	// 딜러번호
	@Column(nullable = false, unique = true, length = 20)
	private String dealerNumber;
	
	// 딜러명
	@Column(nullable = false, length = 50)
	private String dealerName;
	
	// 로그인 비밀번호
	@Column(nullable = false, length = 255)
	private String dealerPassword;
	
	// 연락처
	@Column(nullable = false, length = 20)
	private String dealerPhone;
	
	// 이메일
	@Column(nullable = false, unique = true, length = 100)
	private String dealerEmail;
	
	// 담당지점 (서울, 인천, 대구, 부산, 제주지점)
	@Column(nullable = false, length = 50)
	private String branchName;
	
	// 계정상태(정상, 휴직, 퇴사)
	@Column(nullable = false, length = 20)
	private String accountStatus;
	
	// 입사일
	@Column(nullable = false)
	private LocalDate hireDate;
	
	// 등록일시
	@Column(nullable = false)
	private LocalDateTime createdAt;
	
	// 수정일시
	@Column(nullable = false)
	private LocalDateTime updatedAt;
	
	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
		
		if (this.accountStatus == null || this.accountStatus.isBlank()) {
			this.accountStatus = "정상";
		}
	}
	
	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}
