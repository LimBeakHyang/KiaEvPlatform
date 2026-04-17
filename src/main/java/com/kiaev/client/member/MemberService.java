package com.kiaev.client.member;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public void join(Member member) {
    	Member existingByLoginId = memberRepository.findByLoginId(member.getLoginId()).orElse(null);
    	Member existingByEmail = memberRepository.findByEmail(member.getEmail()).orElse(null);

        // 비밀번호 확인 체크
        if (member.getMemberPw() == null || member.getConfirmPw() == null ||
            !member.getMemberPw().equals(member.getConfirmPw())) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        // joinDate 기본값
        if (member.getJoinDate() == null) {
            member.setJoinDate(LocalDate.now());
        }

        // memberStatus 기본값
        if (member.getMemberStatus() == null || member.getMemberStatus().isBlank()) {
            member.setMemberStatus("정상회원");
        }

        // 1. 같은 아이디가 있고 정상회원이면 가입 불가
        if (existingByLoginId != null && !"탈퇴회원".equals(existingByLoginId.getMemberStatus())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        // 2. 같은 이메일이 있고 정상회원이면 가입 불가
        if (existingByEmail != null && !"탈퇴회원".equals(existingByEmail.getMemberStatus())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 3. 같은 아이디의 탈퇴회원이면 재가입 처리
        if (existingByLoginId != null && "탈퇴회원".equals(existingByLoginId.getMemberStatus())) {
            existingByLoginId.setMemberPw(member.getMemberPw());
            existingByLoginId.setConfirmPw(member.getConfirmPw());
            existingByLoginId.setMemberName(member.getMemberName());
            existingByLoginId.setEmail(member.getEmail());
            existingByLoginId.setPhone(member.getPhone());
            existingByLoginId.setAddress(member.getAddress());
            existingByLoginId.setDetailAddress(member.getDetailAddress());
            existingByLoginId.setMemberStatus("정상회원");
            existingByLoginId.setJoinDate(LocalDate.now());

            memberRepository.save(existingByLoginId);
            return;
        }

        // 4. 같은 이메일의 탈퇴회원이면 재가입 처리
        if (existingByEmail != null && "탈퇴회원".equals(existingByEmail.getMemberStatus())) {
            existingByEmail.setLoginId(member.getLoginId());
            existingByEmail.setMemberPw(member.getMemberPw());
            existingByEmail.setConfirmPw(member.getConfirmPw());
            existingByEmail.setMemberName(member.getMemberName());
            existingByEmail.setEmail(member.getEmail());
            existingByEmail.setPhone(member.getPhone());
            existingByEmail.setAddress(member.getAddress());
            existingByEmail.setDetailAddress(member.getDetailAddress());
            existingByEmail.setMemberStatus("정상회원");
            existingByEmail.setJoinDate(LocalDate.now());

            memberRepository.save(existingByEmail);
            return;
        }

        // 5. 완전 신규 회원 저장
        memberRepository.save(member);
    }

    public void delete(Long memberNo) {
        memberRepository.deleteById(memberNo);
    }

    public boolean existsByLoginId(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }
}