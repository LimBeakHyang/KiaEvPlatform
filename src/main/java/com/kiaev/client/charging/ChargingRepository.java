package com.kiaev.client.charging;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargingRepository extends JpaRepository<Charging, String> {
	
	/* // location을 기준으로 충전소 목록을 조회하는 메서드 추가
    List<Charging> findByLocation(String location); */
    
	/*
 // 수정 후: 해당 단어가 '포함'된 모든 결과를 찾음 (location LIKE "%강남%")
    List<Charging> findByLocationContaining(String location); */
    
 // 이름(statNm)이나 주소(addr)에 검색어가 '포함'된 데이터를 찾습니다.
    List<Charging> findByStatNmContainingOrAddrContaining(String statNm, String addr);
}
