package com.kiaev.client.login;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Login, Long> {

    Optional<Login> findByLoginId(String loginId);

    Optional<Login> findByMemberNameAndEmail(String memberName, String email);

    Optional<Login> findByLoginIdAndEmail(String loginId, String email);
}