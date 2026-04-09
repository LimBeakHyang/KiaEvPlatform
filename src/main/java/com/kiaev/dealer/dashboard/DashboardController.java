package com.kiaev.dealer.dashboard;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kiaev.dealer.login.DealerLogin;

import jakarta.servlet.http.HttpSession;

/**
 * 딜러 대시보드 Controller
 *
 * 주요 기능 1. 로그인한 딜러만 접근 가능 2. 딜러 메인 페이지 제공 3. 기존 /dealer/dashboard 접근 시
 * /dealer/main 으로 리다이렉트 4. 통계 화면 제공
 *
 * 보완 사항 - dashboard null 방지 - 월별 / 차량별 통계 리스트 null 방지 - 차트용 labels / counts /
 * amounts 분리
 */
@Controller
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;

	/**
	 * 딜러 메인 페이지
	 *
	 * 기존 dashboardMain.html 을 메인 화면으로 사용합니다.
	 */
	@GetMapping("/dealer/main")
	public String dealerMain(HttpSession session, Model model) {

		// 세션에서 로그인 딜러 정보 조회
		DealerLogin loginDealer = (DealerLogin) session.getAttribute("loginDealer");

		// 로그인되지 않은 경우 로그인 페이지로 이동
		if (loginDealer == null) {
			return "redirect:/dealer/login";
		}

		// 딜러 메인 화면용 요약 정보 조회
		Dashboard dashboard = dashboardService.getDashboardSummary(loginDealer.getDealerNo(),
				loginDealer.getDealerName());

		model.addAttribute("dashboard", dashboard);
		model.addAttribute("loginDealer", loginDealer);

		return "dealer/dashboard/dashboardMain";
	}

	/**
	 * 기존 /dealer/dashboard URL 접근 시 /dealer/main 으로 이동시켜 기존 링크와 호환합니다.
	 */
	@GetMapping("/dealer/dashboard")
	public String dashboardRedirect(HttpSession session) {

		DealerLogin loginDealer = (DealerLogin) session.getAttribute("loginDealer");

		if (loginDealer == null) {
			return "redirect:/dealer/login";
		}

		return "redirect:/dealer/main";
	}

	/**
	 * 통계 화면
	 *
	 * 제공 데이터 1. 요약 통계 2. 월별 판매 통계 3. 차량 종류별 판매 통계 4. Chart.js 에 사용할 배열 데이터
	 */
	@GetMapping("/dealer/statistics")
	public String statisticsMain(HttpSession session, Model model) {

		// 로그인 딜러 확인
		DealerLogin loginDealer = (DealerLogin) session.getAttribute("loginDealer");

		// 로그인 안 된 경우 로그인 페이지로 이동
		if (loginDealer == null) {
			return "redirect:/dealer/login";
		}

		Integer dealerNo = loginDealer.getDealerNo();
		String dealerName = loginDealer.getDealerName();

		// =========================
		// 1. 요약 통계 조회
		// =========================
		Dashboard dashboard = dashboardService.getDashboardSummary(dealerNo, dealerName);

		// =========================
		// 2. 월별 판매 통계 조회
		// =========================
		List<MonthlySalesStat> monthlySalesStats = dashboardService.getMonthlySalesStats(dealerNo);

		if (monthlySalesStats == null) {
			monthlySalesStats = new ArrayList<>();
		}

		// 차트용 데이터 분리
		List<String> monthlyLabels = new ArrayList<>();
		List<Integer> monthlyCounts = new ArrayList<>();
		List<Integer> monthlyAmounts = new ArrayList<>();

		for (MonthlySalesStat stat : monthlySalesStats) {
			if (stat == null) {
				continue;
			}

			monthlyLabels.add(stat.getSalesMonth() != null ? stat.getSalesMonth() : "");
			monthlyCounts.add(stat.getSalesCount() != null ? stat.getSalesCount() : 0);
			monthlyAmounts.add(stat.getSalesAmount() != null ? stat.getSalesAmount() : 0);
		}

		// =========================
		// 3. 차량 종류별 판매 통계 조회
		// =========================
		List<CarModelSalesStat> carModelSalesStats = dashboardService.getCarModelSalesStats(dealerNo);

		if (carModelSalesStats == null) {
			carModelSalesStats = new ArrayList<>();
		}

		// 차트용 데이터 분리
		List<String> carModelLabels = new ArrayList<>();
		List<Integer> carModelCounts = new ArrayList<>();
		List<Integer> carModelAmounts = new ArrayList<>();

		for (CarModelSalesStat stat : carModelSalesStats) {
			if (stat == null) {
				continue;
			}

			carModelLabels.add(stat.getModelName() != null ? stat.getModelName() : "");
			carModelCounts.add(stat.getSalesCount() != null ? stat.getSalesCount() : 0);
			carModelAmounts.add(stat.getSalesAmount() != null ? stat.getSalesAmount() : 0);
		}

		// =========================
		// 4. 화면 전달
		// =========================
		model.addAttribute("dashboard", dashboard);
		model.addAttribute("loginDealer", loginDealer);

		model.addAttribute("monthlySalesStats", monthlySalesStats);
		model.addAttribute("carModelSalesStats", carModelSalesStats);

		model.addAttribute("monthlyLabels", monthlyLabels);
		model.addAttribute("monthlyCounts", monthlyCounts);
		model.addAttribute("monthlyAmounts", monthlyAmounts);

		model.addAttribute("carModelLabels", carModelLabels);
		model.addAttribute("carModelCounts", carModelCounts);
		model.addAttribute("carModelAmounts", carModelAmounts);

		return "dealer/dashboard/statisticsMain";
	}
}