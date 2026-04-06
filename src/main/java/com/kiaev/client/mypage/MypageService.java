package com.kiaev.client.mypage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MypageService {

    @Autowired
    private MypageRepository mypageRepository;

    // 내 정보 조회
    public Mypage getMemberInfo(Long memberNo) {
        return mypageRepository.findMemberInfoByMemberNo(memberNo);
    }

    // 내 정보 수정
    public void updateMemberInfo(Mypage mypage) {
        mypageRepository.updateMemberInfo(mypage);
    }

    // 비밀번호 변경
    public boolean changePassword(Long memberNo, String currentPw, String newPw) {
        return mypageRepository.updatePassword(memberNo, currentPw, newPw);
    }

    // 회원 탈퇴
    public void deleteMember(Long memberNo) {
        mypageRepository.deleteMember(memberNo);
    }

    // 상담 내역 조회
    public List<Mypage> getConsultHistory(Long memberNo) {
        return mypageRepository.findConsultHistoryByMemberNo(memberNo);
    }

    // 문의 내역 조회
    public List<Mypage> getBoardHistory(Long memberNo) {
        return mypageRepository.findConsultHistoryByMemberNo(memberNo);
    }

    // 관심 차량 조회
    public List<Mypage> getFavoriteCars(Long memberNo) {
        return mypageRepository.findFavoriteCarsByMemberNo(memberNo);
    }
    // 관심 차량 삭제
    public void deleteWishlist(Long wishlistNo, Long memberNo) {
        mypageRepository.deleteWishlist(wishlistNo, memberNo);
    }
    public List<Wishlist> getWishlist(Long memberNo) {
        return mypageRepository.getWishlist(memberNo);
    }
    public void toggleWishlist(Long memberNo, Long carId) {
        mypageRepository.toggleWishlist(memberNo, carId);
    }
}