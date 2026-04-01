package com.kiaev.client.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

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
        return "redirect:/";
    }

    // 아이디 찾기 화면
    @GetMapping("/client/login/findId")
    public String findIdForm() {
        return "client/login/findId";
    }

    // 아이디 찾기 처리
    @PostMapping("/client/login/findId")
    public String findId(@RequestParam("memberName") String memberName,
                         @RequestParam("email") String email,
                         Model model) {

        Login user = loginService.findId(memberName, email);

        if (user == null) {
            model.addAttribute("findIdFail", true);
            return "client/login/findId";
        }

        model.addAttribute("findIdSuccess", true);
        model.addAttribute("foundLoginId", user.getLoginId());
        return "client/login/findId";
    }

    // 비밀번호 찾기 화면
    @GetMapping("/client/login/findPw")
    public String findPwForm() {
        return "client/login/findPw";
    }

    // 1단계: 본인확인
    @PostMapping("/client/login/findPw")
    public String findPw(@RequestParam("loginId") String loginId,
                         @RequestParam("email") String email,
                         Model model) {

        Login user = loginService.findPw(loginId, email);

        if (user == null) {
            model.addAttribute("findPwFail", true);
            return "client/login/findPw";
        }

        model.addAttribute("findPwSuccess", true);
        model.addAttribute("loginId", loginId);
        model.addAttribute("email", email);
        return "client/login/findPw";
    }

    // 2단계: 같은 findPw.html에서 새 비밀번호 변경
    @PostMapping("/client/login/updatePw")
    public String updatePw(@RequestParam("loginId") String loginId,
                           @RequestParam("email") String email,
                           @RequestParam("newPassword") String newPassword,
                           @RequestParam("confirmPassword") String confirmPassword,
                           Model model) {

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("findPwSuccess", true);
            model.addAttribute("loginId", loginId);
            model.addAttribute("email", email);
            model.addAttribute("resetPwError", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return "client/login/findPw";
        }

        boolean result = loginService.updatePassword(loginId, email, newPassword);

        if (!result) {
            model.addAttribute("findPwFail", true);
            return "client/login/findPw";
        }

        model.addAttribute("resetPwSuccess", true);
        return "client/login/loginForm";
    }
}