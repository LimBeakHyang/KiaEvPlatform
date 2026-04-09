package com.kiaev.dealer.sales;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kiaev.dealer.dealerconsult.DealerConsult;
import com.kiaev.dealer.login.DealerLogin;

import jakarta.servlet.http.HttpSession;

/**
 * 판매 관리 Controller
 * 
 * 기능 - 판매 목록 조회 - 판매 상세 조회 - 판매 등록 화면 조회 - 완료/진행중 상담 선택 - 판매 등록 처리
 */
@Controller
public class SalesController {

	@Autowired
	private SalesService salesService;

	/**
	 * 판매 목록 조회
	 * 
	 * 로그인한 딜러의 판매 목록만 조회합니다. 로그인 안 되어 있으면 로그인 페이지로 이동합니다.
	 * 
	 * URL: GET /dealer/sales/list
	 */
	@GetMapping("/dealer/sales/list")
	public String salesList(HttpSession session, Model model) {

		// 세션에서 로그인 딜러 정보 조회
		DealerLogin loginDealer = (DealerLogin) session.getAttribute("loginDealer");

		// 로그인 안 된 경우 로그인 페이지로 이동
		if (loginDealer == null) {
			return "redirect:/dealer/login";
		}

		// 로그인한 딜러번호
		Integer dealerNo = loginDealer.getDealerNo();

		// 로그인한 딜러 기준 판매 목록 조회
		List<Sales> salesList = salesService.getSalesListByDealerNo(dealerNo);

		// 화면 전달
		model.addAttribute("salesList", salesList);
		model.addAttribute("loginDealer", loginDealer);

		return "dealer/sales/salesList";
	}

	/**
	 * 판매 상세 조회
	 * 
	 * 로그인한 딜러 본인의 판매내역만 조회합니다.
	 * 
	 * URL: GET /dealer/sales/detail?salesNo=1
	 */
	@GetMapping("/dealer/sales/detail")
	public String salesDetail(@RequestParam("salesNo") Integer salesNo, HttpSession session, Model model) {

		// 세션에서 로그인 딜러 정보 조회
		DealerLogin loginDealer = (DealerLogin) session.getAttribute("loginDealer");

		// 로그인 안 된 경우 로그인 페이지로 이동
		if (loginDealer == null) {
			return "redirect:/dealer/login";
		}

		Integer dealerNo = loginDealer.getDealerNo();

		// 본인 판매 상세 조회
		Sales sales = salesService.getSalesDetailByDealerNo(salesNo, dealerNo);

		// 데이터가 없으면 목록으로 이동
		if (sales == null) {
			return "redirect:/dealer/sales/list";
		}

		model.addAttribute("sales", sales);
		model.addAttribute("loginDealer", loginDealer);

		return "dealer/sales/salesDetail";
	}

	/**
	 * 판매 등록 화면 조회
	 * 
	 * 진행중 / 완료 상태의 상담 중, 아직 판매 등록되지 않은 상담만 선택 가능하게 합니다.
	 * 
	 * consultNo가 함께 넘어오면 해당 상담을 자동 선택해서 폼에 반영합니다.
	 * 
	 * URL: GET /dealer/sales/register URL: GET /dealer/sales/register?consultNo=1
	 */
	@GetMapping("/dealer/sales/register")
	public String salesRegisterForm(@RequestParam(value = "consultNo", required = false) Integer consultNo,
			HttpSession session, Model model) {

		// 세션에서 로그인 딜러 정보 조회
		DealerLogin loginDealer = (DealerLogin) session.getAttribute("loginDealer");

		// 로그인 안 된 경우 로그인 페이지로 이동
		if (loginDealer == null) {
			return "redirect:/dealer/login";
		}

		Integer dealerNo = loginDealer.getDealerNo();

		// 판매 등록 가능한 상담 목록 조회
		List<DealerConsult> completedConsultList = salesService.getCompletedConsultList(dealerNo);

		// 기본은 빈 객체
		Sales sales = new Sales();

		// consultNo가 넘어오면 해당 상담으로 폼 자동 채움
		if (consultNo != null) {
			Sales selectedSales = salesService.createSalesFromConsult(consultNo, dealerNo);

			if (selectedSales != null) {
				sales = selectedSales;
			} else {
				model.addAttribute("errorMessage", "선택한 상담으로 판매 등록을 진행할 수 없습니다.");
			}
		}

		// 화면 전달
		model.addAttribute("consultList", completedConsultList);
		model.addAttribute("completedConsultList", completedConsultList);
		model.addAttribute("selectedConsultNo", consultNo);
		model.addAttribute("sales", sales);
		model.addAttribute("loginDealer", loginDealer);

		return "dealer/sales/salesRegister";
	}

	/**
	 * 완료/진행중 상담 선택 후 판매 등록 화면 반영
	 * 
	 * URL: GET /dealer/sales/register/select?consultNo=1
	 */
	@GetMapping("/dealer/sales/register/select")
	public String selectConsultForSales(@RequestParam("consultNo") Integer consultNo, HttpSession session) {

		// 세션에서 로그인 딜러 정보 조회
		DealerLogin loginDealer = (DealerLogin) session.getAttribute("loginDealer");

		// 로그인 안 된 경우 로그인 페이지로 이동
		if (loginDealer == null) {
			return "redirect:/dealer/login";
		}

		/**
		 * 선택용 URL은 유지하되, 실제 화면 렌더링은 /dealer/sales/register 로 넘겨서 진입 경로를 하나로 통일합니다.
		 */
		return "redirect:/dealer/sales/register?consultNo=" + consultNo;
	}

	/**
	 * 판매 등록 처리
	 * 
	 * URL: POST /dealer/sales/register
	 */
	@PostMapping("/dealer/sales/register")
	public String salesRegister(@RequestParam("consultNo") Integer consultNo,
			@RequestParam("salesAmount") Integer salesAmount, HttpSession session, Model model) {

		// 세션에서 로그인 딜러 정보 조회
		DealerLogin loginDealer = (DealerLogin) session.getAttribute("loginDealer");

		// 로그인 안 된 경우 로그인 페이지로 이동
		if (loginDealer == null) {
			return "redirect:/dealer/login";
		}

		Integer dealerNo = loginDealer.getDealerNo();

		// 판매 등록 처리
		String resultMessage = salesService.registerSales(consultNo, dealerNo, salesAmount);

		// 성공이면 판매 목록으로 이동
		if ("판매 등록이 완료되었습니다.".equals(resultMessage)) {
			return "redirect:/dealer/sales/list";
		}

		// 실패 시 다시 등록 화면으로 이동하면서 메시지 출력
		List<DealerConsult> completedConsultList = salesService.getCompletedConsultList(dealerNo);
		Sales sales = salesService.createSalesFromConsult(consultNo, dealerNo);

		// sales가 null이면 화면 바인딩 에러 방지용 기본 객체 생성
		if (sales == null) {
			sales = new Sales();
			sales.setConsultNo(consultNo);
		}

		model.addAttribute("consultList", completedConsultList);
		model.addAttribute("completedConsultList", completedConsultList);
		model.addAttribute("sales", sales);
		model.addAttribute("selectedConsultNo", consultNo);
		model.addAttribute("errorMessage", resultMessage);
		model.addAttribute("loginDealer", loginDealer);

		return "dealer/sales/salesRegister";
	}
}