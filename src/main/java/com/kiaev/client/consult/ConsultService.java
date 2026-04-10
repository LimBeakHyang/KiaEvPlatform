package com.kiaev.client.consult;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsultService {

    private final ConsultRepository consultationRepository;

    // 상담 저장
    public Consult save(Consult consultation) {
        return consultationRepository.save(consultation);
    }

    // 상담번호로 조회 (관리자/딜러용 전체 조회)
    public Consult findById(Long consultNo) {
        return consultationRepository.findById(consultNo).orElse(null);
    }

    // 상담번호 + 회원번호로 조회 (일반회원 본인 확인용)
    public Consult findByConsultNoAndMemberNo(Long consultNo, Long memberNo) {
        return consultationRepository.findByConsultNoAndMemberNo(consultNo, memberNo).orElse(null);
    }

    // 전체 조회
    public List<Consult> findAll() {
        return consultationRepository.findAll();
    }

    // 회원번호로 상담 목록 조회 (마이페이지용)
    public List<Consult> findByMemberNo(Long memberNo) {
        return consultationRepository.findByMemberNoOrderByRequestDateDesc(memberNo);
    }
}