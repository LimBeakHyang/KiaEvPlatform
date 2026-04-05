package com.kiaev.admin.login;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminLoginRepository extends JpaRepository<AdminLogin, Long> {
	
	Optional<AdminLogin> findByAdminIdAndAdminPw(String adminId, String adminPw);
}
