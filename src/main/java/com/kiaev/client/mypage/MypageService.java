package com.kiaev.client.mypage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MypageService {

    @Autowired
    private MypageRepository mypageRepository;

    public Mypage getMemberInfo(Long memberNo) {
        return mypageRepository.findMemberInfoByMemberNo(memberNo);
    }

    public void updateMemberInfo(Mypage mypage) {
        mypageRepository.updateMemberInfo(mypage);
    }

    public boolean changePassword(Long memberNo, String currentPw, String newPw) {
        return mypageRepository.updatePassword(memberNo, currentPw, newPw);
    }

    public List<Mypage> getConsultHistory(Long memberNo) {
        return mypageRepository.findConsultHistoryByMemberNo(memberNo);
    }

    public List<Mypage> getFavoriteCars(Long memberNo) {
        return mypageRepository.findFavoriteCarsByMemberNo(memberNo);
    }
}