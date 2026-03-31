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

    // 로그인 화면
    @GetMapping("/login")
    public String loginForm() {
        return "client/login/loginForm";
    }

    // 아이디/비밀번호 찾기 화면
    @GetMapping("/client/login/findIdPw")
    public String findIdPwForm() {
        return "client/login/findIdPw";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(
            @RequestParam("loginId") String loginId,
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

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // 아이디 찾기 처리
    @PostMapping("/login/findId")
    public String findId(
            @RequestParam("memberName") String memberName,
            @RequestParam("email") String email,
            Model model) {

        String foundId = loginService.findId(memberName, email);

        if (foundId == null) {
            model.addAttribute("findIdMsg", "일치하는 아이디가 없습니다.");
        } else {
            model.addAttribute("findIdMsg", "회원님의 아이디는 [ " + foundId + " ] 입니다.");
        }

        return "client/login/findIdPw";
    }

    // 비밀번호 찾기 -> 정보 확인 후 같은 페이지에서 재설정 폼 보여주기
    @PostMapping("/login/findPw")
    public String findPw(
            @RequestParam("loginId") String loginId,
            @RequestParam("email") String email,
            Model model) {

        boolean exists = loginService.checkMemberForPasswordReset(loginId, email);

        if (!exists) {
            model.addAttribute("findPwMsg", "입력한 정보와 일치하는 회원이 없습니다.");
            return "client/login/findIdPw";
        }

        model.addAttribute("resetMode", true);
        model.addAttribute("loginId", loginId);
        model.addAttribute("email", email);
        model.addAttribute("findPwMsg", "회원 확인이 완료되었습니다. 새 비밀번호를 입력해주세요.");

        return "client/login/findIdPw";
    }

    // 비밀번호 재설정 처리
    @PostMapping("/login/resetPw")
    public String resetPw(
            @RequestParam("loginId") String loginId,
            @RequestParam("email") String email,
            @RequestParam("newPw") String newPw,
            @RequestParam("confirmPw") String confirmPw,
            Model model) {

        if (!newPw.equals(confirmPw)) {
            model.addAttribute("resetMode", true);
            model.addAttribute("loginId", loginId);
            model.addAttribute("email", email);
            model.addAttribute("resetPwMsg", "새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return "client/login/findIdPw";
        }

        boolean result = loginService.resetPassword(loginId, email, newPw);

        if (!result) {
            model.addAttribute("resetMode", true);
            model.addAttribute("loginId", loginId);
            model.addAttribute("email", email);
            model.addAttribute("resetPwMsg", "비밀번호 변경에 실패했습니다.");
            return "client/login/findIdPw";
        }

        model.addAttribute("msg", "비밀번호가 성공적으로 변경되었습니다. 다시 로그인해주세요.");
        return "client/login/loginForm";
    }
}