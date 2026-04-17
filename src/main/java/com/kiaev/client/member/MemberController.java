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

import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    // 약관동의 화면
    @GetMapping("/member/terms")
    public String termsForm() {
        return "client/member/termsForm";
    }

    // 약관동의 처리
    @PostMapping("/member/terms")
    public String agreeTerms(
            @RequestParam(value = "agreeService", required = false) String agreeService,
            @RequestParam(value = "agreePrivacy", required = false) String agreePrivacy,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (agreeService == null || agreePrivacy == null) {
            redirectAttributes.addFlashAttribute("termsError", "필수 약관에 동의해야 회원가입이 가능합니다.");
            return "redirect:/member/terms";
        }

        session.setAttribute("termsAgreed", true);
        return "redirect:/member/join";
    }

    // 회원가입 화면
    @GetMapping("/member/join")
    public String joinForm(HttpSession session, RedirectAttributes redirectAttributes) {
        Boolean termsAgreed = (Boolean) session.getAttribute("termsAgreed");

        if (termsAgreed == null || !termsAgreed) {
            redirectAttributes.addFlashAttribute("termsError", "약관동의 후 회원가입이 가능합니다.");
            return "redirect:/member/terms";
        }

        return "client/member/joinForm";
    }

    // 회원가입 저장
    @PostMapping("/member/join")
    public String join(Member member, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            memberService.join(member);
            session.removeAttribute("termsAgreed");
            redirectAttributes.addFlashAttribute("joinSuccess", "회원가입이 완료되었습니다. 로그인해주세요.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("joinError", e.getMessage());
            return "redirect:/member/join";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("joinError", "회원가입 중 오류가 발생했습니다.");
            return "redirect:/member/join";
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
    
    // [수정된 회원 탈퇴 처리]
    @PostMapping("/member/delete")
    public String withdraw(@RequestParam("memberNo") Long memberNo, // HTML 폼에서 넘긴 번호를 직접 받음
                           HttpSession session, 
                           RedirectAttributes redirectAttributes) {
        
        System.out.println("★ [MemberController] 탈퇴 요청 수신. 회원번호: " + memberNo);
        
        try {
            // 1. 서비스 호출하여 DB 상태값 변경
            memberService.delete(memberNo);

            // 2. 세션 무효화 (로그아웃 처리)
            session.invalidate();

            redirectAttributes.addFlashAttribute("joinSuccess", "탈퇴가 정상적으로 완료되었습니다.");
            return "redirect:/";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "탈퇴 처리 중 오류가 발생했습니다.");
            return "redirect:/mypage/info"; // 실패 시 내 정보 페이지로
        }
    }
}