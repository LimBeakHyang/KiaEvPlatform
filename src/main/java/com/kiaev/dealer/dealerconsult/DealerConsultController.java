package com.kiaev.dealer.dealerconsult;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kiaev.dealer.login.DealerLogin;

import jakarta.servlet.http.HttpSession;

/**
 * 상담 관리 Controller
 * 
 * 기능 - 상담 목록 조회 - 상담 상세 조회 - 상담 상태 변경
 * 
 * 참고: - 딜러는 consult_memo(상담일지/상담메모)를 조회만 합니다. - 메모 저장 / 수정 기능은 이 Controller 에서
 * 처리하지 않습니다.
 */
@Controller
public class DealerConsultController {

	@Autowired
	private DealerConsultService consultService;

	/**
	 * 상담 목록 조회
	 * 
	 * 로그인한 딜러에게 배정된 상담 목록만 조회합니다. 로그인 안 되어 있으면 로그인 페이지로 이동합니다.
	 * 
	 * URL: GET /dealer/consult/list
	 */
	@GetMapping("/dealer/consult/list")
	public String consultList(HttpSession session, Model model) {

		// 세션에서 로그인 딜러 정보 조회
		DealerLogin loginDealer = (DealerLogin) session.getAttribute("loginDealer");

		// 로그인 안 되어 있으면 로그인 페이지로 이동
		if (loginDealer == null) {
			return "redirect:/dealer/login";
		}

		// 로그인한 딜러 번호
		Integer dealerNo = loginDealer.getDealerNo();

		// 해당 딜러의 상담 목록 조회
		List<DealerConsult> consultList = consultService.getConsultList(dealerNo);

		// 화면 전달
		model.addAttribute("consultList", consultList);
		model.addAttribute("loginDealer", loginDealer);

		return "dealer/consult/consultList";
	}

	/**
	 * 상담 상세 조회
	 * 
	 * 상담번호 + 로그인 딜러번호가 일치하는 경우만 조회합니다. 다른 딜러 상담은 조회할 수 없도록 막습니다.
	 * 
	 * URL: GET /dealer/consult/detail?consultNo=1
	 */
	@GetMapping("/dealer/consult/detail")
	public String consultDetail(@RequestParam("consultNo") Integer consultNo, HttpSession session, Model model) {

		// 세션에서 로그인 딜러 정보 조회
		DealerLogin loginDealer = (DealerLogin) session.getAttribute("loginDealer");

		// 로그인 안 되어 있으면 로그인 페이지로 이동
		if (loginDealer == null) {
			return "redirect:/dealer/login";
		}

		// 로그인한 딜러 번호
		Integer dealerNo = loginDealer.getDealerNo();

		// 상담 상세 조회
		DealerConsult consult = consultService.getConsultDetail(consultNo, dealerNo);

		// 해당 상담이 없거나 다른 딜러 상담이면 목록으로 이동
		if (consult == null) {
			return "redirect:/dealer/consult/list";
		}

		// 화면 전달
		model.addAttribute("consult", consult);
		model.addAttribute("loginDealer", loginDealer);

		return "dealer/consult/consultDetail";
	}

	/**
	 * 상담 상태 변경
	 * 
	 * 상태값 - 대기 / WAITING - 진행중 / IN_PROGRESS - 완료 / COMPLETED
	 * 
	 * URL: POST /dealer/consult/status
	 */
	@PostMapping("/dealer/consult/status")
	public String updateConsultStatus(@RequestParam("consultNo") Integer consultNo,
			@RequestParam("consultStatus") String consultStatus, HttpSession session) {

		// 세션에서 로그인 딜러 정보 조회
		DealerLogin loginDealer = (DealerLogin) session.getAttribute("loginDealer");

		// 로그인 안 되어 있으면 로그인 페이지로 이동
		if (loginDealer == null) {
			return "redirect:/dealer/login";
		}

		// 로그인한 딜러 번호
		Integer dealerNo = loginDealer.getDealerNo();

		// 상담 상태 변경
		consultService.updateConsultStatus(consultNo, dealerNo, consultStatus);

		/**
		 * 핵심 흐름 상담 상태를 "완료"로 변경한 경우에는 상담 상세로 돌아가지 않고 판매 등록 화면으로 바로 연결합니다.
		 * 
		 * 그리고 방금 완료한 consultNo를 함께 넘겨서 판매 등록 화면에서 자동 선택되도록 처리합니다.
		 */
		if ("완료".equals(consultStatus) || "COMPLETED".equals(consultStatus)) {
			return "redirect:/dealer/sales/register/select?consultNo=" + consultNo;
		}

		// 완료가 아니면 기존처럼 상세 페이지로 다시 이동
		return "redirect:/dealer/consult/detail?consultNo=" + consultNo;
	}
}