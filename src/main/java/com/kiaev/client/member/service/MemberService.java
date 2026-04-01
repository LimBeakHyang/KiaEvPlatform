package com.kiaev.client.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kiaev.client.member.domain.Member;
import com.kiaev.client.member.repository.MemberRepository;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public void join(Member member) {
        if (memberRepository.existsByLoginId(member.getLoginId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        memberRepository.save(member);
    }

    public boolean isLoginIdDuplicate(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    public boolean isEmailDuplicate(String email) {
        return memberRepository.existsByEmail(email);
    }
}