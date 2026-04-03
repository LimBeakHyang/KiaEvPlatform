package com.kiaev.client.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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