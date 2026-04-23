package com.kiaev.admin.consult;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminConsultController {
	
	private final AdminConsultService adminConsultService;
	
	@GetMapping("/admin/consult/list")
	public String consultList(Model model) {
		model.addAttribute("comsultList", adminConsultService.getConsultList());
		return "admin/consult/adminConsultList";
	}
	
	@GetMapping("/admin/consult/detail")
	public String consultDetail(@RequestParam("consultId") Long consultId, Model model) {
		model.addAttribute("consult", adminConsultService.getConsultDetail(consultId));
		return "admin/consult/adminConsultDetail";
	}
	
	@PostMapping("/admin/consult/assign")
	public String assign(@RequestParam("consultId") Long consultId, 
						@RequestParam("dealerNumber") String dealerNumber,
						@RequestParam("dealerName") String dealerName) {
		
		adminConsultService.assignDealer(consultId, dealerNumber, dealerName);
		return "redirect:/admin/consult/detail?consultId=" + consultId;
	}
	
	@PostMapping("/admin/consult/update")
	public String updateMemo(@RequestParam("consultId") Long consultId,
							@RequestParam("adminMemo") String adminmemo) {
		
		adminConsultService.updateMemo(consultId, adminmemo);
		return "redirect:/admin/consult/detail?consultId=" + consultId;
	}
	
}
