package com.kiaev.dealer.dashboard;

/**
 * 차량 종류별 판매 통계 DTO
 *
 * 예: - EV3 : 3건 - EV6 : 5건 - EV9 : 2건
 */
public class CarModelSalesStat {

	// =========================
	// 차량 모델명
	// =========================
	private String modelName;

	// =========================
	// 판매 건수
	// =========================
	private Integer salesCount;

	// =========================
	// 판매 금액 합계
	// =========================
	private Integer salesAmount;

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
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