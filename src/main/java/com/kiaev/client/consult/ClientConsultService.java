package com.kiaev.client.consult;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientConsultService {

    private final ClientConsultRepository consultationRepository;

    // 상담 저장
    public ClientConsult save(ClientConsult consultation) {
        return consultationRepository.save(consultation);
    }

    // 상담번호로 조회 (관리자/딜러용 전체 조회)
    public ClientConsult findById(Long consultNo) {
        return consultationRepository.findById(consultNo).orElse(null);
    }

    // 상담번호 + 회원번호로 조회 (일반회원 본인 확인용)
    public ClientConsult findByConsultNoAndMemberNo(Long consultNo, Long memberNo) {
        return consultationRepository.findByConsultNoAndMemberNo(consultNo, memberNo).orElse(null);
    }

    // 전체 상담 조회
    public List<ClientConsult> findAll() {
        return consultationRepository.findAll();
    }
}