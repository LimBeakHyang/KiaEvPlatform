package com.kiaev.client.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.kiaev.client.member.domain.Member;
import com.kiaev.client.member.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    // 회원가입 페이지 열기
    @GetMapping("/member/join")
    public String joinForm() {
        return "client/member/joinForm";
    }

    // 회원가입 저장
    @PostMapping("/member/join")
    public String join(Member member) {
        memberService.join(member);
        return "redirect:/login";
    }

    // 회원탈퇴
    @GetMapping("/member/delete")
    public String deleteMember(HttpSession session) {
        Object userObj = session.getAttribute("loginUser");

        if (userObj != null) {
            com.kiaev.client.login.Login user = (com.kiaev.client.login.Login) userObj;
            memberService.delete(user.getMemberNo());
            session.invalidate();
        }

        return "redirect:/login";
    }
}