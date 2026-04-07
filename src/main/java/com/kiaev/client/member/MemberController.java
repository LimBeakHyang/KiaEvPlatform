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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String join(Member member, Model model, RedirectAttributes redirectAttributes) {
        try {
            memberService.join(member);

            // 회원가입 성공 메시지 전달
            model.addAttribute("joinSuccess", "회원가입이 완료되었습니다. 로그인해주세요.");
            
            // 다시 회원가입 페이지를 열어줌
            // 여기서 메시지창을 띄울 예정
            return "client/member/joinForm";


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
    // 이메일 중복확인
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