package com.kiaev.client.login;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    // 로그인 페이지
    @GetMapping("/login")
    public String loginForm() {
        return "client/login/loginForm";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam("loginId") String loginId,
                        @RequestParam("memberPw") String memberPw,
                        HttpSession session,
                        Model model) {

        Login loginUser = loginService.login(loginId, memberPw);

        if (loginUser == null) {
            model.addAttribute("loginError", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "client/login/loginForm";
        }

        session.setAttribute("loginUser", loginUser);

        // 메인페이지로 이동
        return "redirect:/";
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // 아이디 찾기 페이지
    @GetMapping("/client/login/findId")
    public String findIdForm() {
        return "client/login/findId";
    }

    // 아이디 찾기 처리
    @PostMapping("/client/login/findId")
    public String findId(@RequestParam("memberName") String memberName,
                         @RequestParam("email") String email,
                         Model model) {

        if (memberName == null || memberName.trim().isEmpty()
                || email == null || email.trim().isEmpty()) {
            model.addAttribute("errorMessage", "정보를 모두 입력해주세요.");
            return "client/login/findId";
        }

        String foundId = loginService.findLoginId(memberName, email);

        if (foundId != null) {
            model.addAttribute("foundId", foundId);
        } else {
            model.addAttribute("errorMessage", "입력하신 정보와 일치하는 아이디가 없습니다.");
        }

        return "client/login/findId";
    }

    // 비밀번호 찾기 페이지
    @GetMapping("/client/login/findPw")
    public String findPwForm() {
        return "client/login/findPw";
    }

    // 비밀번호 확인
    @PostMapping("/client/login/findPw")
    public String findPw(@RequestParam("loginId") String loginId,
                         @RequestParam("memberName") String memberName,
                         @RequestParam("email") String email,
                         Model model) {

        if (loginId == null || loginId.trim().isEmpty()
                || memberName == null || memberName.trim().isEmpty()
                || email == null || email.trim().isEmpty()) {
            model.addAttribute("errorMessage", "정보를 모두 입력해주세요.");
            return "client/login/findPw";
        }

        boolean exists = loginService.checkMemberForPasswordReset(loginId, memberName, email);

        if (exists) {
            model.addAttribute("verified", true);
            model.addAttribute("loginId", loginId);
        } else {
            model.addAttribute("errorMessage", "입력하신 정보와 일치하는 회원정보가 없습니다.");
        }

        return "client/login/findPw";
    }

    // 새 비밀번호 저장
    @PostMapping("/client/login/resetPw")
    public String resetPw(@RequestParam("loginId") String loginId,
                          @RequestParam("newPassword") String newPassword,
                          @RequestParam("confirmPassword") String confirmPassword,
                          Model model) {

        if (newPassword == null || newPassword.trim().isEmpty()
                || confirmPassword == null || confirmPassword.trim().isEmpty()) {
            model.addAttribute("verified", true);
            model.addAttribute("loginId", loginId);
            model.addAttribute("resetError", "새 비밀번호를 모두 입력해주세요.");
            return "client/login/findPw";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("verified", true);
            model.addAttribute("loginId", loginId);
            model.addAttribute("resetError", "비밀번호가 일치하지 않습니다.");
            return "client/login/findPw";
        }

        loginService.updatePassword(loginId, newPassword);
        model.addAttribute("resetSuccess", "비밀번호가 변경되었습니다.");
        return "client/login/findPw";
    }
}