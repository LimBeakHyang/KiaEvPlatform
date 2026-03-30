package com.kiaev.client.login;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    // 로그인
    public Login login(String loginId, String memberPw) {
        Optional<Login> optionalLogin = loginRepository.findByLoginId(loginId);

        if (optionalLogin.isEmpty()) {
            return null;
        }

        Login login = optionalLogin.get();

        if (!login.getMemberPw().equals(memberPw)) {
            return null;
        }

        return login;
    }

    // 아이디 찾기
    public String findId(String memberName, String email) {
        Optional<Login> optionalLogin = loginRepository.findByMemberNameAndEmail(memberName, email);

        if (optionalLogin.isEmpty()) {
            return null;
        }

        return optionalLogin.get().getLoginId();
    }

    // 비밀번호 재설정용 회원 확인
    public boolean checkMemberForPasswordReset(String loginId, String email) {
        Optional<Login> optionalLogin = loginRepository.findByLoginIdAndEmail(loginId, email);
        return optionalLogin.isPresent();
    }

    // 비밀번호 재설정
    public boolean resetPassword(String loginId, String email, String newPw) {
        Optional<Login> optionalLogin = loginRepository.findByLoginIdAndEmail(loginId, email);

        if (optionalLogin.isEmpty()) {
            return false;
        }

        Login login = optionalLogin.get();
        login.setMemberPw(newPw);
        loginRepository.save(login);

        return true;
    }
}