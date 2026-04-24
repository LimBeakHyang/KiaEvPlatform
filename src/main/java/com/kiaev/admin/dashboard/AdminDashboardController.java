package com.kiaev.admin.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminDashboardController {
	
	private final AdminDashboardService adminDashboardService;
	
	@GetMapping("/admin/main")
	public String adminmain(Model model) {
		AdminDashboard dashboard = adminDashboardService.getDashboardInfo();
		model.addAttribute("dashboard", dashboard);
		return "admin/dashboard/adminMain";
	}
}
