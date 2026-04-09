package com.kiaev.client.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);

    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
    
 // 아이디와 비밀번호로 회원 찾기 (로그인용)
    Member findByLoginIdAndMemberPw(String loginId, String memberPw);
}
