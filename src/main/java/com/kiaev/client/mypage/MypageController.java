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
	@GetMapping("/mypage/myinfo")
	public String myInfo(HttpSession session, Model model) {
		Login loginUser = (Login) session.getAttribute("loginUser");

		// 로그인 안 되어 있으면 로그인 페이지로 이동
		if (loginUser == null) {
			return "redirect:/login";
		}

		// 회원 정보 조회
		Mypage memberInfo = mypageService.getMemberInfo(loginUser.getMemberNo());

		model.addAttribute("loginUser", loginUser);
		model.addAttribute("memberInfo", memberInfo);

		return "client/mypage/myinfo";
	}

	// 내 정보 수정 처리
	@PostMapping("/mypage/update")
	public String updateMyInfo(Mypage mypage, HttpSession session) {
		Login loginUser = (Login) session.getAttribute("loginUser");

		// 로그인 안 되어 있으면 로그인 페이지로 이동
		if (loginUser == null) {
			return "redirect:/login";
		}

		// 세션에 저장된 회원번호 기준으로 수정
		mypage.setMemberNo(loginUser.getMemberNo());
		mypageService.updateMemberInfo(mypage);

		return "redirect:/mypage/myinfo";
	}

	// 비밀번호 변경 페이지 이동
	@GetMapping("/mypage/passwordChange")
	public String passwordForm(HttpSession session, Model model) {
		Login loginUser = (Login) session.getAttribute("loginUser");

		// 비밀번호 변경 성공 후에는 세션이 끊긴 상태에서도
		// flash 값(passwordChangeSuccess)이 있으면 페이지를 한 번 보여주도록 처리
		if (loginUser == null && !model.containsAttribute("passwordChangeSuccess")) {
			return "redirect:/login";
		}

		return "client/mypage/passwordChange";
	}

	// 비밀번호 변경 처리
	@PostMapping("/mypage/passwordChange")
	public String passwordChange(@RequestParam("currentPw") String currentPw, // 현재 비밀번호
			@RequestParam("newPw") String newPw, // 새 비밀번호
			@RequestParam("confirmPw") String confirmPw, // 새 비밀번호 확인
			HttpSession session, RedirectAttributes redirectAttributes) {

		// 로그인 사용자 정보 가져오기
		Login loginUser = (Login) session.getAttribute("loginUser");

		// 로그인 정보가 없으면 로그인 페이지로 이동
		if (loginUser == null) {
			return "redirect:/login";
		}

		// 새 비밀번호와 새 비밀번호 확인이 다르면 다시 비밀번호 변경 페이지로 이동
		if (!newPw.equals(confirmPw)) {
			redirectAttributes.addFlashAttribute("errorMessage", "새 비밀번호가 일치하지 않습니다.");
			return "redirect:/mypage/passwordChange";
		}

		// 비밀번호 변경 서비스 호출
		boolean result = mypageService.changePassword(loginUser.getMemberNo(), currentPw, newPw);

		// 현재 비밀번호가 틀리면 실패 메시지 출력
		if (!result) {
			redirectAttributes.addFlashAttribute("errorMessage", "현재 비밀번호가 올바르지 않습니다.");
			return "redirect:/mypage/passwordChange";
		}

		// 비밀번호 변경 성공 시 세션 종료
		session.invalidate();

		// 비밀번호 변경 페이지에서 성공 알럿을 띄우기 위한 flash 값 전달
		redirectAttributes.addFlashAttribute("passwordChangeSuccess", true);

		// 다시 비밀번호 변경 페이지로 이동
		// -> 이 페이지에서 alert 띄운 후 로그인 페이지로 이동할 예정
		return "redirect:/mypage/passwordChange";
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

	// 내 정보 수정 화면
	@GetMapping("/mypage/update")
	public String updateForm(HttpSession session, Model model) {
		Login loginUser = (Login) session.getAttribute("loginUser");

		// 로그인 안 되어 있으면 로그인 페이지로 이동
		if (loginUser == null) {
			return "redirect:/login";
		}

		// 회원 정보 조회
		Mypage memberInfo = mypageService.getMemberInfo(loginUser.getMemberNo());

		model.addAttribute("loginUser", loginUser);
		model.addAttribute("memberInfo", memberInfo);

		return "client/mypage/update";
	}
}