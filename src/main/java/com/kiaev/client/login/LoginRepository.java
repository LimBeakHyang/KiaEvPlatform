package com.kiaev.client.login;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Login, Long> {

    Login findByLoginIdAndMemberPw(String loginId, String memberPw);

    Login findByMemberNameAndEmail(String memberName, String email);

    Login findByLoginIdAndMemberNameAndEmail(String loginId, String memberName, String email);

    Login findByLoginId(String loginId);
}