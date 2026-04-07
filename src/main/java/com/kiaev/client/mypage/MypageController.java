package com.kiaev.client.mypage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kiaev.client.login.Login;
import com.kiaev.client.login.LoginService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MypageController {

    @Autowired
    private MypageService mypageService;
    
    @Autowired
    private LoginService loginService;

    // 마이페이지 메인 화면
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

    // 내 정보 조회 화면
    @GetMapping("/mypage/myinfo")
    public String myInfo(HttpSession session, Model model) {
        Login loginUser = (Login) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        Mypage memberInfo = mypageService.getMemberInfo(loginUser.getMemberNo());

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("memberInfo", memberInfo);

        return "client/mypage/myinfo";
    }

    // 내 정보 수정 처리
    @PostMapping("/mypage/update")
    public String updateMyInfo(Mypage mypage, HttpSession session) {
        Login loginUser = (Login) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        mypage.setMemberNo(loginUser.getMemberNo());
        mypageService.updateMemberInfo(mypage);

        return "redirect:/mypage/myinfo";
    }

    // 비밀번호 변경 페이지 이동
    @GetMapping("/mypage/passwordChange")
    public String passwordForm(HttpSession session) {
        Login loginUser = (Login) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        return "client/mypage/passwordChange";
    }

    // 비밀번호 변경 처리
    @PostMapping("/mypage/passwordChange")
    public String passwordChange(
            @RequestParam("currentPw") String currentPw,
            @RequestParam("newPw") String newPw,
            @RequestParam("confirmPw") String confirmPw,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Login loginUser = (Login) session.getAttribute("loginUser");

        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("timeoutMessage", "오랫동안 이용이 없어 로그아웃되었습니다.");
            return "redirect:/login";
        }

        if (!newPw.equals(confirmPw)) {
            redirectAttributes.addFlashAttribute("errorMessage", "새 비밀번호가 일치하지 않습니다.");
            return "redirect:/mypage/passwordChange";
        }

        boolean result = mypageService.changePassword(loginUser.getMemberNo(), currentPw, newPw);

        if (!result) {
            redirectAttributes.addFlashAttribute("errorMessage", "현재 비밀번호가 올바르지 않습니다.");
            return "redirect:/mypage/passwordChange";
        }

        session.invalidate();

        redirectAttributes.addFlashAttribute("successMessage", "비밀번호가 정상적으로 변경되었습니다. 다시 로그인해주세요.");

        return "redirect:/login";
    }

    // 회원 탈퇴 처리
    @GetMapping("/mypage/delete")
    public String deleteMember(HttpSession session) {
        Login loginUser = (Login) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        mypageService.deleteMember(loginUser.getMemberNo());

        session.invalidate();

        return "redirect:/login";
    }

    // 상담내역 조회
    @GetMapping("/mypage/consult")
    public String consultHistory(HttpSession session, Model model) {
        Login loginUser = (Login) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        List<Mypage> list = mypageService.getConsultHistory(loginUser.getMemberNo());

        model.addAttribute("consultList", list);
        model.addAttribute("loginUser", loginUser);

        return "client/mypage/consultHistory";
    }

    // 문의 내역 조회
    @GetMapping("/mypage/board")
    public String boardHistory(HttpSession session, Model model) {
        Login loginUser = (Login) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        List<Mypage> list = mypageService.getBoardHistory(loginUser.getMemberNo());

        model.addAttribute("boardList", list);
        model.addAttribute("loginUser", loginUser);

        return "client/mypage/boardHistory";
    }

    // 내 정보 수정 화면
    @GetMapping("/mypage/update")
    public String updateForm(HttpSession session, Model model) {
        Login loginUser = (Login) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        Mypage memberInfo = mypageService.getMemberInfo(loginUser.getMemberNo());

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("memberInfo", memberInfo);

        return "client/mypage/update";
    }
}