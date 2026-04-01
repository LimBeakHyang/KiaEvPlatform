package com.kiaev.client.login;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {

    // 회원 로그인
    Optional<Login> findByLoginIdAndMemberPw(String loginId, String memberPw);

    // 딜러 로그인
    Optional<Login> findByDealerIdAndMemberPw(String dealerId, String memberPw);

    // 아이디 찾기
    Optional<Login> findByMemberNameAndEmail(String memberName, String email);

    // 비밀번호 찾기
    Optional<Login> findByLoginIdAndEmail(String loginId, String email);
}