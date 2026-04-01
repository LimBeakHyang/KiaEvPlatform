package com.kiaev.client.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kiaev.client.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);

    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
}