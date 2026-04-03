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
        memberRepository.save(member);
    }

    public void delete(Long memberNo) {
        memberRepository.deleteById(memberNo);
    }
}