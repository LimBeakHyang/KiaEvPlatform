package com.kiaev.admin.dealer;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class AdminDealerService {
	
	private final AdminDealerRepository adminDealerRepository;
	
	public AdminDealerService(AdminDealerRepository adminDealerRepository) {
		this.adminDealerRepository = adminDealerRepository;
	}
	
	// 딜러 전체 목록 조회
	@Transactional(readOnly = true)
	public List<AdminDealer> findAllDealers() {
		return adminDealerRepository.findAll();
	}
	
	// 딜러 상세 조회
    @Transactional(readOnly = true)
    public AdminDealer findDealerById(Long dealerId) {
        return adminDealerRepository.findById(dealerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 딜러 정보를 찾을 수 없습니다. dealerId=" + dealerId));
    }
    
    // 딜러번호로 조회
    @Transactional(readOnly = true)
    public AdminDealer findDealerByDealerNumber(String dealerNumber) {
        return adminDealerRepository.findByDealerNumber(dealerNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 딜러번호의 딜러를 찾을 수 없습니다. dealerNumber=" + dealerNumber));
    }
    
    // 신규 딜러 등록
    public AdminDealer registerDealer(AdminDealer adminDealer) {
        validateDuplicateDealerNumber(adminDealer.getDealerNumber());
        validateDuplicateDealerEmail(adminDealer.getDealerEmail());

        if (adminDealer.getAccountStatus() == null || adminDealer.getAccountStatus().isBlank()) {
            adminDealer.setAccountStatus("정상");
        }

        return adminDealerRepository.save(adminDealer);
    }
    
    // 딜러 정보 수정
    public AdminDealer updateDealer(Long dealerId, AdminDealer updatedDealer) {
        AdminDealer dealer = findDealerById(dealerId);

        if (!dealer.getDealerNumber().equals(updatedDealer.getDealerNumber())) {
            validateDuplicateDealerNumber(updatedDealer.getDealerNumber());
        }

        if (!dealer.getDealerEmail().equals(updatedDealer.getDealerEmail())) {
            validateDuplicateDealerEmail(updatedDealer.getDealerEmail());
        }

        dealer.setDealerNumber(updatedDealer.getDealerNumber());
        dealer.setDealerName(updatedDealer.getDealerName());
        dealer.setDealerPassword(updatedDealer.getDealerPassword());
        dealer.setDealerPhone(updatedDealer.getDealerPhone());
        dealer.setDealerEmail(updatedDealer.getDealerEmail());
        dealer.setBranchName(updatedDealer.getBranchName());
        dealer.setAccountStatus(updatedDealer.getAccountStatus());
        dealer.setHireDate(updatedDealer.getHireDate());

        return dealer;
    }
    
    // 딜러 상태 변경
    public void updateDealerStatus(Long dealerId, String accountStatus) {
        AdminDealer dealer = findDealerById(dealerId);
        dealer.setAccountStatus(accountStatus);
    }
    
    // 딜러 삭제 대신 퇴사 처리
    public void resignDealer(Long dealerId) {
        AdminDealer dealer = findDealerById(dealerId);
        dealer.setAccountStatus("퇴사");
    }
    
    // 담당 지점 필터
    @Transactional(readOnly = true)
    public List<AdminDealer> findDealersByBranchName(String branchName) {
        return adminDealerRepository.findByBranchName(branchName);
    }
    
 // 계정 상태 필터
    @Transactional(readOnly = true)
    public List<AdminDealer> findDealersByAccountStatus(String accountStatus) {
        return adminDealerRepository.findByAccountStatus(accountStatus);
    }

    // 담당 지점 + 계정 상태 필터
    @Transactional(readOnly = true)
    public List<AdminDealer> findDealersByBranchAndStatus(String branchName, String accountStatus) {
        return adminDealerRepository.findByBranchNameAndAccountStatus(branchName, accountStatus);
    }

    // 딜러번호 중복 검사
    @Transactional(readOnly = true)
    public void validateDuplicateDealerNumber(String dealerNumber) {
        if (adminDealerRepository.findByDealerNumber(dealerNumber).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 딜러번호입니다.");
        }
    }

    // 이메일 중복 검사
    @Transactional(readOnly = true)
    public void validateDuplicateDealerEmail(String dealerEmail) {
        if (adminDealerRepository.findByDealerEmail(dealerEmail).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
    }
    
}
