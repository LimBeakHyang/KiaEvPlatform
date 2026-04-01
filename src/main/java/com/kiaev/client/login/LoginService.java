package com.kiaev.client.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    // 회원 로그인
    public Login login(String loginId, String memberPw) {
        return loginRepository.findByLoginIdAndMemberPw(loginId, memberPw).orElse(null);
    }

    // 딜러 로그인
    public Login dealerLogin(String dealerId, String memberPw) {
        return loginRepository.findByDealerIdAndMemberPw(dealerId, memberPw).orElse(null);
    }

    // 아이디 찾기
    public Login findId(String memberName, String email) {
        return loginRepository.findByMemberNameAndEmail(memberName, email).orElse(null);
    }

    // 비밀번호 찾기
    public Login findPw(String loginId, String email) {
        return loginRepository.findByLoginIdAndEmail(loginId, email).orElse(null);
    }

    // 비밀번호 변경
    public boolean updatePassword(String loginId, String email, String newPassword) {
        Login user = loginRepository.findByLoginIdAndEmail(loginId, email).orElse(null);

        if (user == null) {
            return false;
        }

        user.setMemberPw(newPassword);
        loginRepository.save(user);
        return true;
    }
}