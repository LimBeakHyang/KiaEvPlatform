package com.kiaev.dealer.dashboard;

/**
 * 월별 판매 통계 DTO
 */
public class MonthlySalesStat {

	// =========================
	// 판매 월 (예: 2026-04)
	// =========================
	private String salesMonth;

	// =========================
	// 판매 건수
	// =========================
	private Integer salesCount;

	// =========================
	// 판매 금액 합계
	// =========================
	private Integer salesAmount;

	public String getSalesMonth() {
		return salesMonth;
	}

	public void setSalesMonth(String salesMonth) {
		this.salesMonth = salesMonth;
	}

	public Integer getSalesCount() {
		return salesCount;
	}

	public void setSalesCount(Integer salesCount) {
		this.salesCount = salesCount;
	}

	public Integer getSalesAmount() {
		return salesAmount;
	}

	public void setSalesAmount(Integer salesAmount) {
		this.salesAmount = salesAmount;
	}
}