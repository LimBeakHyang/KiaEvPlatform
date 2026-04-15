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
    public String loginForm(
            @RequestParam(value = "pwChanged", required = false) String pwChanged,
            @RequestParam(value = "timeout", required = false) String timeout,
            Model model) {

        if ("true".equals(pwChanged)) {
            model.addAttribute("successMessage", "비밀번호가 정상적으로 변경되었습니다. 다시 로그인해주세요.");
        }

        if ("true".equals(timeout)) {
            model.addAttribute("timeoutMessage", "장시간 이용이 없어 로그아웃되었습니다.");
        }

        return "client/login/loginForm";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam("loginId") String loginId,
                        @RequestParam("memberPw") String memberPw,
                        HttpSession session,
                        Model model) {

        try {
            Login loginUser = loginService.login(loginId, memberPw);

            if (loginUser == null) {
                model.addAttribute("loginError", "아이디 또는 비밀번호가 일치하지 않습니다.");
                return "client/login/loginForm";
            }

            // 로그인 성공 시 세션 저장
            session.setAttribute("loginUser", loginUser);

            // 세션 유지시간 30분
            session.setMaxInactiveInterval(1800);

            // 로그인 전 가려던 페이지가 있으면 그쪽으로 이동
            String prevPage = (String) session.getAttribute("prevPage");

            if (prevPage != null) {
                session.removeAttribute("prevPage");
                return "redirect:" + prevPage;
            }

            // 기본은 메인페이지 이동
            return "redirect:/";

        } catch (IllegalArgumentException e) {
            model.addAttribute("loginError", e.getMessage());
            return "client/login/loginForm";
        }
    }
    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // ================================
    // 추가 기능 2 : 세션 만료 안내용 전용 주소
    // 다른 컨트롤러에서 세션이 없을 때
    // return "redirect:/session-expired";
    // 이렇게 보내면 로그인 페이지로 이동하면서 안내문구가 뜸
    // ================================
    @GetMapping("/session-expired")
    public String sessionExpired() {
        return "redirect:/login?timeout=true";
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
