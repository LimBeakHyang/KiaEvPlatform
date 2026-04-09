package com.kiaev.client.mypage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.kiaev.client.login.Login;

import jakarta.servlet.http.HttpSession;

@Controller
public class MypageController {

    @Autowired
    private MypageService mypageService;

    // 마이페이지 메인
    @GetMapping("/mypage")
    public String mypageMain(HttpSession session, Model model) {
        Login loginUser = (Login) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        List<Mypage> favoriteCars = mypageService.getFavoriteCars(loginUser.getMemberNo());

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("favoriteCars", favoriteCars);

        return "client/mypage/MypageMain";
    }

    // 내 정보
    @GetMapping("/mypage/info")
    public String myInfo(HttpSession session, Model model) {
        Login loginUser = (Login) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        Mypage memberInfo = mypageService.getMemberInfo(loginUser.getMemberNo());

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("memberInfo", memberInfo);

        return "client/mypage/myInfo";
    }

    // 정보 수정
    @PostMapping("/mypage/update")
    public String updateMyInfo(Mypage mypage, HttpSession session) {
        Login loginUser = (Login) session.getAttribute("loginUser");

        mypage.setMemberNo(loginUser.getMemberNo());
        mypageService.updateMemberInfo(mypage);

        return "redirect:/mypage/info";
    }

    // 🔥 비밀번호 변경
    @PostMapping("/mypage/password")
    public String changePassword(
            @RequestParam("currentPw") String currentPw,
            @RequestParam("newPw") String newPw,
            HttpSession session,
            Model model) {

        Login loginUser = (Login) session.getAttribute("loginUser");

        boolean result = mypageService.changePassword(loginUser.getMemberNo(), currentPw, newPw);

        if (!result) {
            model.addAttribute("error", "현재 비밀번호가 틀렸습니다.");
            return "client/mypage/myInfo";
        }

        return "redirect:/mypage/info";
    }

    // 상담내역
    @GetMapping("/mypage/consult")
    public String consultHistory(HttpSession session, Model model) {
        Login loginUser = (Login) session.getAttribute("loginUser");

        List<Mypage> list = mypageService.getConsultHistory(loginUser.getMemberNo());

        model.addAttribute("consultList", list);
        return "client/mypage/consultHistory";
    }
}