package com.kiaev.dealer.sales;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 판매 엔티티
 * 
 * sales_tbl 테이블과 매핑됩니다. 판매 등록 1건의 정보를 저장합니다.
 * 
 * 주의: - 현재 프로젝트에서는 차량번호(car_no)를 문자열(String)로 관리합니다. - 예: "123가1111"
 */
@Entity
@Table(name = "sales_tbl")
public class Sales {

	// =========================
	// 기본 식별 정보
	// =========================

	// 판매번호(PK)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sales_no")
	private Integer salesNo;

	// 상담번호
	@Column(name = "consult_no")
	private Integer consultNo;

	// 회원번호
	@Column(name = "member_no")
	private Integer memberNo;

	// 차량번호
	// 현재 프로젝트에서는 문자열 차량번호 사용
	@Column(name = "car_no")
	private String carNo;

	// 딜러번호
	@Column(name = "dealer_no")
	private Integer dealerNo;

	// =========================
	// 판매 정보
	// =========================

	// 판매일시
	@Column(name = "sales_date")
	private LocalDateTime salesDate;

	// 판매금액
	@Column(name = "sales_amount")
	private Integer salesAmount;

	// 판매상태
	// 예: COMPLETED
	@Column(name = "sales_status")
	private String salesStatus;

	// =========================
	// getter / setter
	// =========================

	public Integer getSalesNo() {
		return salesNo;
	}

	public void setSalesNo(Integer salesNo) {
		this.salesNo = salesNo;
	}

	public Integer getConsultNo() {
		return consultNo;
	}

	public void setConsultNo(Integer consultNo) {
		this.consultNo = consultNo;
	}

	public Integer getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(Integer memberNo) {
		this.memberNo = memberNo;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public Integer getDealerNo() {
		return dealerNo;
	}

	public void setDealerNo(Integer dealerNo) {
		this.dealerNo = dealerNo;
	}

	public LocalDateTime getSalesDate() {
		return salesDate;
	}

	public void setSalesDate(LocalDateTime salesDate) {
		this.salesDate = salesDate;
	}

	public Integer getSalesAmount() {
		return salesAmount;
	}

	public void setSalesAmount(Integer salesAmount) {
		this.salesAmount = salesAmount;
	}

	public String getSalesStatus() {
		return salesStatus;
	}

	public void setSalesStatus(String salesStatus) {
		this.salesStatus = salesStatus;
	}
}