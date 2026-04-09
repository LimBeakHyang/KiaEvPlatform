package com.kiaev.dealer.sales;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 판매 Repository
 * 
 * 기능 - 딜러별 판매 목록 조회 - 딜러별 판매 상세 조회 - 상담번호 중복 판매 확인 - 차량번호 중복 판매 확인 - 딜러 기준 판매
 * 건수 조회 - 딜러 기준 판매 총액 조회
 */
public interface SalesRepository extends JpaRepository<Sales, Integer> {

	/**
	 * 로그인한 딜러 기준 판매 목록 조회
	 * 
	 * 판매번호 내림차순 정렬
	 * 
	 * @param dealerNo 딜러번호
	 * @return 해당 딜러의 판매 목록
	 */
	List<Sales> findByDealerNoOrderBySalesNoDesc(Integer dealerNo);

	/**
	 * 로그인한 딜러 본인 판매 상세 조회
	 * 
	 * @param salesNo  판매번호
	 * @param dealerNo 딜러번호
	 * @return 조건에 맞는 판매 1건
	 */
	Optional<Sales> findBySalesNoAndDealerNo(Integer salesNo, Integer dealerNo);

	/**
	 * 이미 판매 등록된 상담인지 확인
	 * 
	 * @param consultNo 상담번호
	 * @return 이미 등록된 상담이면 true
	 */
	boolean existsByConsultNo(Integer consultNo);

	/**
	 * 이미 판매된 차량인지 확인
	 * 
	 * 주의: Sales 엔티티의 carNo 타입이 String 이므로 Repository 메서드도 String 으로 맞춰야 합니다.
	 * 
	 * @param carNo 차량번호
	 * @return 이미 판매된 차량이면 true
	 */
	boolean existsByCarNo(String carNo);

	/**
	 * 딜러 기준 판매 건수 조회
	 * 
	 * @param dealerNo 딜러번호
	 * @return 해당 딜러의 판매 건수
	 */
	long countByDealerNo(Integer dealerNo);

	/**
	 * 딜러 기준 판매 총액 조회
	 * 
	 * 판매 데이터가 없더라도 null 대신 0이 나오도록 COALESCE 처리
	 * 
	 * @param dealerNo 딜러번호
	 * @return 해당 딜러의 총 판매금액
	 */
	@Query("SELECT COALESCE(SUM(s.salesAmount), 0) FROM Sales s WHERE s.dealerNo = :dealerNo")
	Long sumSalesAmountByDealerNo(@Param("dealerNo") Integer dealerNo);
}