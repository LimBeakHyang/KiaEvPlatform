package com.kiaev.client.mypage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kiaev.client.login.Login;

import jakarta.servlet.http.HttpSession;

@Controller
public class MypageController {

    @Autowired
    private MypageService mypageService;

    // 마이페이지 메인 화면
    @GetMapping("/mypage")
    public String mypageMain(HttpSession session, Model model) {
        Login loginUser = (Login) session.getAttribute("loginUser");

        // 로그인 안 되어 있으면 로그인 페이지로 이동
        if (loginUser == null) {
            return "redirect:/login";
        }

        // 관심 차량 목록 조회
        List<Mypage> favoriteCars = mypageService.getFavoriteCars(loginUser.getMemberNo());

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("favoriteCars", favoriteCars);

        return "client/mypage/MypageMain";
    }

    // 내 정보 조회 화면
    @GetMapping("/mypage/info")
    public String myInfo(HttpSession session, Model model) {
        Login loginUser = (Login) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        Mypage memberInfo = mypageService.getMemberInfo(loginUser.getMemberNo());

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("memberInfo", memberInfo);

        return "client/mypage/info";
    }

    // 내 정보 수정 처리
    @PostMapping("/mypage/update")
    public String updateMyInfo(Mypage mypage, HttpSession session) {
        Login loginUser = (Login) session.getAttribute("loginUser");

        // 로그인 안 되어 있으면 로그인 페이지로 이동
        if (loginUser == null) {
            return "redirect:/login";
        }

        // 세션의 회원번호를 기준으로 수정
        mypage.setMemberNo(loginUser.getMemberNo());
        mypageService.updateMemberInfo(mypage);

        return "redirect:/mypage/info";
    }

    // 비밀번호 변경 페이지 이동
    @GetMapping("/mypage/password")
    public String passwordForm(HttpSession session) {
        Login loginUser = (Login) session.getAttribute("loginUser");

        // 로그인 안 되어 있으면 로그인 페이지로 이동
        if (loginUser == null) {
            return "redirect:/login";
        }

        return "client/mypage/passwordChange";
    }

    // 비밀번호 변경 처리
    @PostMapping("/mypage/password")
    public String changePassword(
            @RequestParam("currentPw") String currentPw,
            @RequestParam("newPw") String newPw,
            @RequestParam("confirmPw") String confirmPw,
            HttpSession session,
            Model model) {

        Login loginUser = (Login) session.getAttribute("loginUser");

        // 로그인 안 되어 있으면 로그인 페이지로 이동
        if (loginUser == null) {
            return "redirect:/login";
        }

        // 새 비밀번호와 확인 비밀번호가 다르면 에러
        if (!newPw.equals(confirmPw)) {
            model.addAttribute("error", "새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return "client/mypage/passwordChange";
        }

        // 현재 비밀번호 확인 후 변경
        boolean result = mypageService.changePassword(loginUser.getMemberNo(), currentPw, newPw);

        // 현재 비밀번호가 틀리면 다시 변경 페이지로 이동
        if (!result) {
            model.addAttribute("error", "현재 비밀번호가 틀렸습니다.");
            return "client/mypage/passwordChange";
        }

        // 변경 성공 시 내 정보 페이지로 이동
        return "redirect:/mypage/info";
    }

    // 회원 탈퇴 처리
    @GetMapping("/mypage/delete")
    public String deleteMember(HttpSession session) {
        Login loginUser = (Login) session.getAttribute("loginUser");

        // 로그인 안 되어 있으면 로그인 페이지로 이동
        if (loginUser == null) {
            return "redirect:/login";
        }

        // 회원 탈퇴 처리
        mypageService.deleteMember(loginUser.getMemberNo());

        // 세션 종료
        session.invalidate();

        // 탈퇴 후 로그인 페이지로 이동
        return "redirect:/login";
    }

    // 상담내역 조회
    @GetMapping("/mypage/consult")
    public String consultHistory(HttpSession session, Model model) {
        Login loginUser = (Login) session.getAttribute("loginUser");

        // 로그인 안 되어 있으면 로그인 페이지로 이동
        if (loginUser == null) {
            return "redirect:/login";
        }

        // 상담내역 조회
        List<Mypage> list = mypageService.getConsultHistory(loginUser.getMemberNo());

        model.addAttribute("consultList", list);
        model.addAttribute("loginUser", loginUser);

        return "client/mypage/consultHistory";
    }

    // 문의 내역 조회
    @GetMapping("/mypage/board")
    public String boardHistory(HttpSession session, Model model) {
        Login loginUser = (Login) session.getAttribute("loginUser");

        // 로그인 안 되어 있으면 로그인 페이지로 이동
        if (loginUser == null) {
            return "redirect:/login";
        }

        // 문의 내역 조회
        List<Mypage> list = mypageService.getBoardHistory(loginUser.getMemberNo());

        model.addAttribute("boardList", list);
        model.addAttribute("loginUser", loginUser);

        return "client/mypage/boardHistory";
    }

    
    // 관심 차량 목록
    /*@GetMapping("/mypage/wishlist")
    public String wishlist(HttpSession session, Model model) {
        Login loginUser = (Login) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        List<Wishlist> wishlist = mypageService.getWishlist(loginUser.getMemberNo());

        model.addAttribute("wishlist", wishlist);
        model.addAttribute("loginUser", loginUser);

        return "client/mypage/wishlist";
    }

    // 관심 차량 등록 / 취소
   		@PostMapping("/mypage/wishlist/toggle")
    public String toggleWishlist(@RequestParam("carId") Long carId,
                                 HttpSession session) {
        Login loginUser = (Login) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        mypageService.toggleWishlist(loginUser.getMemberNo(), carId);

        return "redirect:/mypage/wishlist";
    }*/

    // 관심 차량 선택 삭제
   /* @PostMapping("/mypage/wishlist/delete")
    public String deleteWishlist(@RequestParam("wishlistNo") Long wishlistNo,
                                 HttpSession session) {
        Login loginUser = (Login) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        mypageService.deleteWishlist(wishlistNo, loginUser.getMemberNo());

        return "redirect:/mypage/wishlist";
    }*/
 
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