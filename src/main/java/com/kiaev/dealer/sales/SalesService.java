package com.kiaev.dealer.sales;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kiaev.dealer.dealerconsult.DealerConsult;
import com.kiaev.dealer.dealerconsult.DealerConsultRepository;

/**
 * 판매 관리 Service
 * 
 * 기능 - 전체 판매 목록 조회 - 로그인한 딜러 판매 목록 조회 - 판매 상세 조회 - 판매 등록 가능한 상담 목록 조회 - 상담 선택 후
 * 판매 등록용 객체 생성 - 판매 등록 처리 - 딜러별 판매 건수 / 총액 조회
 * 
 * 상태값은 로직/DB 기준 영문 코드로 통일합니다.
 * 
 * sales_status : COMPLETED consult_status : WAITING / IN_PROGRESS / COMPLETED
 * 
 * 단, 기존 한글 데이터도 함께 인식합니다.
 */
@Service
public class SalesService {

	@Autowired
	private SalesRepository salesRepository;

	@Autowired
	private DealerConsultRepository consultRepository;

	/**
	 * 전체 판매 목록 조회
	 */
	public List<Sales> getSalesList() {
		return salesRepository.findAll();
	}

	/**
	 * 로그인한 딜러 판매 목록 조회
	 * 
	 * @param dealerNo 로그인한 딜러번호
	 * @return 해당 딜러의 판매 목록
	 */
	public List<Sales> getSalesListByDealerNo(Integer dealerNo) {

		if (dealerNo == null) {
			return new ArrayList<>();
		}

		return salesRepository.findByDealerNoOrderBySalesNoDesc(dealerNo);
	}

	/**
	 * 판매 상세 조회
	 * 
	 * @param salesNo 판매번호
	 * @return 판매 1건, 없으면 null
	 */
	public Sales getSalesDetail(Integer salesNo) {

		if (salesNo == null) {
			return null;
		}

		return salesRepository.findById(salesNo).orElse(null);
	}

	/**
	 * 로그인한 딜러 본인 판매 상세 조회
	 * 
	 * @param salesNo  판매번호
	 * @param dealerNo 딜러번호
	 * @return 본인 판매건이면 반환, 아니면 null
	 */
	public Sales getSalesDetailByDealerNo(Integer salesNo, Integer dealerNo) {

		if (salesNo == null || dealerNo == null) {
			return null;
		}

		Optional<Sales> optionalSales = salesRepository.findBySalesNoAndDealerNo(salesNo, dealerNo);
		return optionalSales.orElse(null);
	}

	/**
	 * 판매 등록 가능한 상담 목록 조회
	 * 
	 * 현재 흐름상 "상담 진행 후 판매 등록하면서 자동 완료 처리"가 자연스럽기 때문에 진행중 / 완료 상태를 모두 허용합니다.
	 * 
	 * 조건 - 로그인한 딜러의 상담만 조회 - 상담 상태가 진행중(IN_PROGRESS) 또는 완료(COMPLETED) - 이미 판매 등록된
	 * 상담 제외 - 이미 판매된 차량 제외
	 */
	public List<DealerConsult> getCompletedConsultList(Integer dealerNo) {

		// 딜러번호 없으면 빈 목록 반환
		if (dealerNo == null) {
			return new ArrayList<>();
		}

		// 로그인한 딜러의 상담 목록 조회
		List<DealerConsult> consultList = consultRepository.findByDealerNoOrderByConsultNoAsc(dealerNo);

		// 반환할 판매 등록 가능 상담 목록
		List<DealerConsult> availableConsultList = new ArrayList<>();

		for (DealerConsult consult : consultList) {

			// null 방어
			if (consult == null) {
				continue;
			}

			// 진행중 / 완료 상태가 아니면 제외
			if (!isAvailableForSales(consult.getConsultStatus())) {
				continue;
			}

			// 상담번호가 없으면 제외
			if (consult.getConsultNo() == null) {
				continue;
			}

			// 이미 판매 등록된 상담이면 제외
			if (salesRepository.existsByConsultNo(consult.getConsultNo())) {
				continue;
			}

			// 차량번호가 없으면 제외
			if (consult.getCarNo() == null) {
				continue;
			}

			/**
			 * 주의: Consult의 carNo 타입과 Sales의 carNo 타입이 프로젝트마다 다를 수 있어서 문자열로 통일해서 비교합니다.
			 */
			String carNo = String.valueOf(consult.getCarNo());

			// 이미 판매된 차량이면 제외
			if (salesRepository.existsByCarNo(carNo)) {
				continue;
			}

			availableConsultList.add(consult);
		}

		return availableConsultList;
	}

	/**
	 * 상담 선택 후 판매 등록 폼용 Sales 객체 생성
	 * 
	 * 판매 등록 화면에서 선택한 상담 정보를 바탕으로 미리 값이 채워진 Sales 객체를 만들어 반환합니다.
	 * 
	 * @param consultNo 상담번호
	 * @param dealerNo  로그인한 딜러번호
	 * @return 화면 바인딩용 Sales 객체, 조건 불충족 시 null
	 */
	public Sales createSalesFromConsult(Integer consultNo, Integer dealerNo) {

		// 필수값 체크
		if (consultNo == null || dealerNo == null) {
			return null;
		}

		// 상담 조회
		Optional<DealerConsult> optionalConsult = consultRepository.findByConsultNoAndDealerNo(consultNo, dealerNo);

		// 상담이 없으면 null
		if (optionalConsult.isEmpty()) {
			return null;
		}

		DealerConsult consult = optionalConsult.get();

		// 상담 상태가 진행중 / 완료가 아니면 불가
		if (!isAvailableForSales(consult.getConsultStatus())) {
			return null;
		}

		// 이미 판매 등록된 상담이면 불가
		if (salesRepository.existsByConsultNo(consultNo)) {
			return null;
		}

		// 차량번호 없으면 불가
		if (consult.getCarNo() == null) {
			return null;
		}

		String carNo = String.valueOf(consult.getCarNo());

		// 이미 판매된 차량이면 불가
		if (salesRepository.existsByCarNo(carNo)) {
			return null;
		}

		// 판매 등록 폼에 미리 채워줄 객체 생성
		Sales sales = new Sales();
		sales.setConsultNo(consult.getConsultNo());
		sales.setMemberNo(consult.getMemberNo());
		sales.setCarNo(carNo);
		sales.setDealerNo(consult.getDealerNo());
		sales.setSalesDate(LocalDateTime.now());
		sales.setSalesStatus("COMPLETED");

		return sales;
	}

	/**
	 * 판매 등록 처리
	 * 
	 * 처리 순서 - 상담번호 / 로그인정보 / 판매금액 검증 - 상담 조회 - 판매 가능 상태인지 확인 - 중복 판매 여부 확인 - 판매 저장
	 * - 상담 자동 완료 처리
	 * 
	 * @param consultNo   상담번호
	 * @param dealerNo    로그인한 딜러번호
	 * @param salesAmount 판매금액
	 * @return 처리 결과 메시지
	 */
	@Transactional
	public String registerSales(Integer consultNo, Integer dealerNo, Integer salesAmount) {

		// 상담번호 체크
		if (consultNo == null) {
			return "상담번호가 없습니다.";
		}

		// 로그인 딜러 체크
		if (dealerNo == null) {
			return "로그인 정보가 없습니다.";
		}

		// 판매금액 체크
		if (salesAmount == null || salesAmount <= 0) {
			return "판매금액을 올바르게 입력해주세요.";
		}

		// 상담 조회
		Optional<DealerConsult> optionalConsult = consultRepository.findByConsultNoAndDealerNo(consultNo, dealerNo);

		if (optionalConsult.isEmpty()) {
			return "해당 상담 정보를 찾을 수 없습니다.";
		}

		DealerConsult consult = optionalConsult.get();

		// 진행중 / 완료 상태 상담만 판매 등록 가능
		if (!isAvailableForSales(consult.getConsultStatus())) {
			return "진행중 또는 완료 상태의 상담만 판매 등록할 수 있습니다.";
		}

		// 이미 판매 등록된 상담인지 확인
		if (salesRepository.existsByConsultNo(consultNo)) {
			return "이미 판매 등록이 완료된 상담입니다.";
		}

		// 차량번호 체크
		if (consult.getCarNo() == null) {
			return "차량번호가 없는 상담은 판매 등록할 수 없습니다.";
		}

		String carNo = String.valueOf(consult.getCarNo());

		// 같은 차량번호 중복 판매 방지
		if (salesRepository.existsByCarNo(carNo)) {
			return "이미 판매 완료된 차량입니다.";
		}

		// 판매 엔티티 생성
		Sales sales = new Sales();
		sales.setConsultNo(consult.getConsultNo());
		sales.setCarNo(carNo);
		sales.setMemberNo(consult.getMemberNo());
		sales.setDealerNo(consult.getDealerNo());
		sales.setSalesAmount(salesAmount);
		sales.setSalesDate(LocalDateTime.now());
		sales.setSalesStatus("COMPLETED");

		// 판매 저장
		salesRepository.save(sales);

		// 상담 자동 완료 처리
		consult.setConsultStatus("COMPLETED");

		// 배정일시가 없으면 현재 시간 저장
		if (consult.getAssignedDate() == null) {
			consult.setAssignedDate(LocalDateTime.now());
		}

		// 완료일시 저장
		consult.setCompletedDate(LocalDateTime.now());

		// 상담 저장
		consultRepository.save(consult);

		return "판매 등록이 완료되었습니다.";
	}

	/**
	 * 딜러 기준 판매 건수 조회
	 * 
	 * @param dealerNo 딜러번호
	 * @return 판매 건수
	 */
	public long getSalesCountByDealerNo(Integer dealerNo) {

		if (dealerNo == null) {
			return 0;
		}

		return salesRepository.countByDealerNo(dealerNo);
	}

	/**
	 * 딜러 기준 판매 총액 조회
	 * 
	 * @param dealerNo 딜러번호
	 * @return 총 판매금액
	 */
	public Long getSalesAmountSumByDealerNo(Integer dealerNo) {

		if (dealerNo == null) {
			return 0L;
		}

		Long totalAmount = salesRepository.sumSalesAmountByDealerNo(dealerNo);
		return totalAmount != null ? totalAmount : 0L;
	}

	/**
	 * 판매 등록 가능 상태인지 판별
	 * 
	 * 허용 상태 - 진행중 / IN_PROGRESS - 완료 / COMPLETED
	 * 
	 * @param consultStatus 상담상태
	 * @return 판매 등록 가능 여부
	 */
	private boolean isAvailableForSales(String consultStatus) {
		return isInProgressStatus(consultStatus) || isCompletedStatus(consultStatus);
	}

	/**
	 * 상담 상태가 진행중인지 판별
	 * 
	 * 기존 데이터 호환을 위해 한글/영문 둘 다 인식
	 * 
	 * @param consultStatus 상담상태
	 * @return 진행중 여부
	 */
	private boolean isInProgressStatus(String consultStatus) {
		return "진행중".equals(consultStatus) || "IN_PROGRESS".equals(consultStatus);
	}

	/**
	 * 상담 상태가 완료인지 판별
	 * 
	 * 기존 데이터 호환을 위해 한글/영문 둘 다 인식
	 * 
	 * @param consultStatus 상담상태
	 * @return 완료 여부
	 */
	private boolean isCompletedStatus(String consultStatus) {
		return "완료".equals(consultStatus) || "COMPLETED".equals(consultStatus);
	}
}