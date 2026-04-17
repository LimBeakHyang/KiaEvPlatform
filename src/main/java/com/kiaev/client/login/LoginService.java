package com.kiaev.client.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

	@Autowired
	private LoginRepository loginRepository;
	

	// 로그인
	public Login login(String loginId, String memberPw) {
		    
		Login login = loginRepository.findByLoginId(loginId);

		// 1. 아이디 존재 여부 확인
		if (login == null) {
			return null;
		}

		// 만약의 경우를 대비해 "탈퇴"라는 글자가 포함되어 있는지도 함께 체크
		String status = (login.getMemberStatus() != null) ? login.getMemberStatus().trim() : "";

		if ("탈퇴회원".equals(status) || status.contains("탈퇴")) {
			throw new IllegalArgumentException("탈퇴된 회원입니다.");
		}

		// 2. 비밀번호 불일치 확인
		if (!login.getMemberPw().equals(memberPw)) {
			return null;
		}

		return login;
	}

	// 아이디 찾기
	public String findLoginId(String memberName, String email) {
		Login login = loginRepository.findByMemberNameAndEmail(memberName, email);

		if (login != null) {
			return login.getLoginId();
		}
		return null;
	}

	// 비밀번호 재설정 전 회원 확인
	public boolean checkMemberForPasswordReset(String loginId, String memberName, String email) {
		Login login = loginRepository.findByLoginIdAndMemberNameAndEmail(loginId, memberName, email);
		return login != null;
	}

	// 비밀번호 변경
	public void updatePassword(String loginId, String newPassword) {
		Login login = loginRepository.findByLoginId(loginId);

		if (login != null) {
			login.setMemberPw(newPassword);
			loginRepository.save(login);
		}
	}
}
