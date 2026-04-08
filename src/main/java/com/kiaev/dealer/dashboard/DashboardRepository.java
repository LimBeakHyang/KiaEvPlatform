package com.kiaev.dealer.dashboard;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * 딜러 통계 Repository
 *
 * 역할 - 로그인한 딜러 기준 요약 통계 조회 - 월별 판매 통계 조회 - 차량 종류별 판매 통계 조회
 *
 * 주의 - 기존 기능은 유지 - 조회 전용 Repository - Native Query 사용 - null 안전 처리 포함
 */
@Repository
public class DashboardRepository {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * 딜러 메인 / 통계 화면 공통 요약 정보 조회
	 *
	 * @param dealerNo   딜러 번호
	 * @param dealerName 딜러명
	 * @return Dashboard 객체
	 */
	public Dashboard getDashboardSummary(Integer dealerNo, String dealerName) {

		Dashboard dashboard = new Dashboard();

		// 기본 딜러 정보 먼저 세팅
		dashboard.setDealerNo(dealerNo);
		dashboard.setDealerName(dealerName);

		// =========================
		// 상담 통계 조회
		// 상태값이 영문 / 한글 둘 다 들어올 가능성 방어
		// =========================
		String consultSql = """
				SELECT
				    COUNT(*) AS total_consult_count,
				    SUM(CASE WHEN consult_status IN ('WAITING', '대기') THEN 1 ELSE 0 END) AS waiting_count,
				    SUM(CASE WHEN consult_status IN ('IN_PROGRESS', '진행중') THEN 1 ELSE 0 END) AS in_progress_count,
				    SUM(CASE WHEN consult_status IN ('COMPLETED', '완료') THEN 1 ELSE 0 END) AS completed_count
				FROM consult_tbl
				WHERE dealer_no = :dealerNo
				""";

		Object[] consultRow = (Object[]) entityManager.createNativeQuery(consultSql).setParameter("dealerNo", dealerNo)
				.getSingleResult();

		dashboard.setTotalConsultCount(toInteger(consultRow[0]));
		dashboard.setWaitingConsultCount(toInteger(consultRow[1]));
		dashboard.setInProgressConsultCount(toInteger(consultRow[2]));
		dashboard.setCompletedConsultCount(toInteger(consultRow[3]));

		// =========================
		// 판매 통계 조회
		// =========================
		String salesSql = """
				SELECT
				    COUNT(*) AS total_sales_count,
				    COALESCE(SUM(sales_amount), 0) AS total_sales_amount
				FROM sales_tbl
				WHERE dealer_no = :dealerNo
				""";

		Object[] salesRow = (Object[]) entityManager.createNativeQuery(salesSql).setParameter("dealerNo", dealerNo)
				.getSingleResult();

		dashboard.setTotalSalesCount(toInteger(salesRow[0]));
		dashboard.setTotalSalesAmount(toInteger(salesRow[1]));

		return dashboard;
	}

	/**
	 * 월별 판매 통계 조회
	 *
	 * @param dealerNo 딜러 번호
	 * @return 월별 판매 통계 목록
	 */
	public List<MonthlySalesStat> getMonthlySalesStats(Integer dealerNo) {

		String sql = """
				SELECT
				    DATE_FORMAT(s.sales_date, '%Y-%m') AS sales_month,
				    COUNT(*) AS sales_count,
				    COALESCE(SUM(s.sales_amount), 0) AS sales_amount
				FROM sales_tbl s
				WHERE s.dealer_no = :dealerNo
				GROUP BY DATE_FORMAT(s.sales_date, '%Y-%m')
				ORDER BY sales_month ASC
				""";

		List<?> results = entityManager.createNativeQuery(sql).setParameter("dealerNo", dealerNo).getResultList();

		List<MonthlySalesStat> list = new ArrayList<>();

		for (Object rowObj : results) {
			Object[] row = (Object[]) rowObj;

			MonthlySalesStat stat = new MonthlySalesStat();
			stat.setSalesMonth(row[0] != null ? row[0].toString() : "");
			stat.setSalesCount(toInteger(row[1]));
			stat.setSalesAmount(toInteger(row[2]));

			list.add(stat);
		}

		return list;
	}

	/**
	 * 차량 종류별 판매 통계 조회
	 *
	 * 핵심 보완 사항 - 기존 INNER JOIN 을 LEFT JOIN 으로 변경 - sales_tbl 에 판매 데이터가 존재하지만
	 * car_tbl 과 조인이 실패하는 경우에도 판매 데이터가 완전히 누락되지 않도록 방어 - model_name 이 null 또는 공백이면
	 * "미분류 차량" 으로 표시
	 *
	 * @param dealerNo 딜러 번호
	 * @return 차량 종류별 판매 통계 목록
	 */
	public List<CarModelSalesStat> getCarModelSalesStats(Integer dealerNo) {

		String sql = """
				SELECT
				    COALESCE(NULLIF(TRIM(c.model_name), ''), '미분류 차량') AS model_name,
				    COUNT(*) AS sales_count,
				    COALESCE(SUM(s.sales_amount), 0) AS sales_amount
				FROM sales_tbl s
				LEFT JOIN car_tbl c
				       ON s.car_no = c.car_no
				WHERE s.dealer_no = :dealerNo
				GROUP BY COALESCE(NULLIF(TRIM(c.model_name), ''), '미분류 차량')
				ORDER BY sales_count DESC, model_name ASC
				""";

		List<?> results = entityManager.createNativeQuery(sql).setParameter("dealerNo", dealerNo).getResultList();

		List<CarModelSalesStat> list = new ArrayList<>();

		for (Object rowObj : results) {
			Object[] row = (Object[]) rowObj;

			CarModelSalesStat stat = new CarModelSalesStat();

			// 차량 모델명
			stat.setModelName(row[0] != null ? row[0].toString() : "미분류 차량");

			// 판매 건수
			stat.setSalesCount(toInteger(row[1]));

			// 판매 금액 합계
			stat.setSalesAmount(toInteger(row[2]));

			list.add(stat);
		}

		return list;
	}

	/**
	 * Object -> Integer 안전 변환
	 *
	 * @param value DB 결과값
	 * @return Integer 값, null이면 0
	 */
	private Integer toInteger(Object value) {
		if (value == null) {
			return 0;
		}

		if (value instanceof Number) {
			return ((Number) value).intValue();
		}

		try {
			return Integer.parseInt(value.toString());
		} catch (Exception e) {
			return 0;
		}
	}
}