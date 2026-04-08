package com.kiaev.client.consult;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientConsultRepository extends JpaRepository<ClientConsult, Long> {

    Optional<ClientConsult> findByConsultNoAndMemberNo(Long consultNo, Long memberNo);
}