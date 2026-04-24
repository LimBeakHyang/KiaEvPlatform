package com.kiaev.admin.login;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminLoginService {
	
	private final AdminLoginRepository adminLoginRepository;
	
	public boolean login(AdminLogin adminLogin) {
		return adminLoginRepository
				.findByAdminIdAndAdminPw(adminLogin.getAdminId(), adminLogin.getAdminPw())
				.isPresent();
	}
}
