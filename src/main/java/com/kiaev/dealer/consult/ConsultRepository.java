package com.kiaev.dealer.consult;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 상담 Repository
 * 
 * Consult 엔티티를 기준으로 상담 데이터를 조회 / 저장합니다.
 */
@Repository
public interface ConsultRepository extends JpaRepository<Consult, Integer> {

	/**
	 * 로그인한 딜러의 상담 목록 조회
	 * 
	 * 상담번호 내림차순 정렬
	 * 
	 * 용도: - 상담 관리 목록 화면
	 * 
	 * @param dealerNo 딜러번호
	 * @return 해당 딜러의 상담 목록
	 */
	List<Consult> findByDealerNoOrderByConsultNoDesc(Integer dealerNo);

	/**
	 * 로그인한 딜러의 상담 목록 조회
	 * 
	 * 상담번호 오름차순 정렬
	 * 
	 * 용도: - 판매 등록 화면 - 순차 선택이 필요한 화면
	 * 
	 * @param dealerNo 딜러번호
	 * @return 해당 딜러의 상담 목록
	 */
	List<Consult> findByDealerNoOrderByConsultNoAsc(Integer dealerNo);

	/**
	 * 상담번호 + 딜러번호가 모두 일치하는 상담 1건 조회
	 * 
	 * 다른 딜러의 상담을 직접 접근하지 못하도록 상세조회 / 상태변경에서 함께 사용합니다.
	 * 
	 * @param consultNo 상담번호
	 * @param dealerNo  딜러번호
	 * @return 조건에 맞는 상담 1건
	 */
	Optional<Consult> findByConsultNoAndDealerNo(Integer consultNo, Integer dealerNo);

	/**
	 * 대시보드용 전체 상담 수 조회
	 * 
	 * @param dealerNo 딜러번호
	 * @return 해당 딜러의 전체 상담 수
	 */
	long countByDealerNo(Integer dealerNo);

	/**
	 * 대시보드용 상태별 상담 수 조회
	 * 
	 * 예: - WAITING - IN_PROGRESS - COMPLETED - 대기 - 진행중 - 완료
	 * 
	 * @param dealerNo      딜러번호
	 * @param consultStatus 상담상태
	 * @return 조건에 맞는 상담 수
	 */
	long countByDealerNoAndConsultStatus(Integer dealerNo, String consultStatus);
}