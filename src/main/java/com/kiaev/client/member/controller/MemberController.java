package com.kiaev.client.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kiaev.client.member.domain.Member;
import com.kiaev.client.member.service.MemberService;

@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/member/join")
    public String joinForm() {
        return "client/member/joinForm";
    }

    @PostMapping("/member/join")
    public String join(Member member, RedirectAttributes redirectAttributes) {
        try {
            memberService.join(member);
            redirectAttributes.addFlashAttribute("joinSuccess", true);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("joinError", e.getMessage());
            return "redirect:/member/join";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("joinError", "회원가입에 실패했습니다.");
            return "redirect:/member/join";
        }
    }

    @PostMapping("/member/checkId")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkId(@RequestParam("loginId") String loginId) {
        Map<String, Object> result = new HashMap<>();

        boolean duplicate = memberService.isLoginIdDuplicate(loginId);

        result.put("duplicate", duplicate);
        result.put("message", duplicate ? "이미 사용중인 아이디입니다." : "사용 가능한 아이디입니다.");

        return ResponseEntity.ok(result);
    }

    @PostMapping("/member/checkEmail")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam("email") String email) {
        Map<String, Object> result = new HashMap<>();

        boolean duplicate = memberService.isEmailDuplicate(email);

        result.put("duplicate", duplicate);
        result.put("message", duplicate ? "이미 존재하는 이메일입니다." : "사용 가능한 이메일입니다.");

        return ResponseEntity.ok(result);
    }
}