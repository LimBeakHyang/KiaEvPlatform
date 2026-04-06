package com.kiaev.client.member;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    // 회원가입 화면
    @GetMapping("/member/join")
    public String joinForm() {
        return "client/member/joinForm";
    }

    // 회원가입 저장
    @PostMapping("/member/join")
    public String join(Member member, Model model) {
        try {
            memberService.join(member);

            return "redirect:/";   // 회원가입 성공 -> 메인페이지

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "redirect:/member/join";   // 실패 -> 회원가입 페이지

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/member/join";   // 오류 -> 회원가입 페이지
        }
    }

    // 아이디 중복확인
    @GetMapping("/member/checkId")
    @ResponseBody
    public Map<String, Object> checkId(@RequestParam("loginId") String loginId) {
        boolean exists = memberService.existsByLoginId(loginId);

        Map<String, Object> result = new HashMap<>();
        result.put("duplicate", exists);
        result.put("message", exists ? "이미 사용중인 아이디 입니다." : "사용 가능한 아이디 입니다.");

        return result;
    }
    @GetMapping("/member/checkEmail")
    @ResponseBody
    public Map<String, Object> checkEmail(@RequestParam("email") String email) {
        boolean exists = memberService.existsByEmail(email);

        Map<String, Object> result = new HashMap<>();
        result.put("duplicate", exists);
        result.put("message", exists ? "이미 사용중인 이메일 입니다." : "사용 가능한 이메일 입니다.");

        return result;
    }
}