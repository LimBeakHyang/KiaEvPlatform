package com.kiaev.admin.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminLoginController {
	
	private final AdminLoginService adminLoginService;
	
	@GetMapping("/login")
	public String loginPage(Model model) {
		model.addAttribute("adminLogin", new AdminLogin());
		return "admin/login/adminLogin";
	}
	
	@PostMapping("/login")
	public String login(AdminLogin adminLogin, HttpSession session, Model model) {
		
		boolean result = adminLoginService.login(adminLogin);
		
		if (result) {
			session.setAttribute("adminId", adminLogin.getAdminId());
			return "redirect:/admin/main";
		} else {
			model.addAttribute("errorMessage", "아이디 혹은 비밀번호가 일치하지 않습니다. ");
			return "admin/login/adminLogin";
		}
	}
}
