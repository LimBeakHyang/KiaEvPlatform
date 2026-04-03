package com.kiaev.admin.dealer;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminDealerRepository extends JpaRepository<AdminDealer, Long> {
	
	// 딜러번호 중복 확인, 상세조회용
	Optional<AdminDealer> findByDealerNumber(String dealerNumber);
	
	// 이메일 중복 확인용
	Optional<AdminDealer> findByDealerEmail(String dealerEmail);
	
	// 계정 상태별 조회
	List<AdminDealer> findByAccountStatus(String accountStatus);
	
	// 담당 지점별 조회
	List<AdminDealer> findByBranchName(String branchName);
	
	// 담당 지점 + 게정 상태 조회
	List<AdminDealer> findByBranchNameAndAccountStatus(String branchName, String accountStatus);
}
