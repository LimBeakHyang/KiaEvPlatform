package com.kiaev.admin.consult;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminConsultService {
	
	private final AdminConsultRepository adminConsultRepository;
	
	public List<AdminConsult> getConsultList() {
		return adminConsultRepository.findAll();
	}
	
	public AdminConsult getConsultDetail(Long consultId) {
		return adminConsultRepository.findById(consultId).orElse(null);
	}
	
	public void assignDealer(Long consultId, String dealerNumber, String dealerName) {
		AdminConsult consult = adminConsultRepository.findById(consultId).orElse(null);
		
		if (consult != null) {
			consult.setDealerNumber(dealerNumber);
			consult.setDealerName(dealerName);
			consult.setConsultStatus("상담 진행");
			adminConsultRepository.save(consult);
		}
	}
	
	public void updateMemo(Long consultId, String adminMemo) {
		AdminConsult consult = adminConsultRepository.findById(consultId).orElse(null);
		
		if (consult != null) {
			consult.setAdminMemo(adminMemo);
			adminConsultRepository.save(consult);
		}
	}
}
