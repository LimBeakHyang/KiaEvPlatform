package com.kiaev.admin.dashboard;

import org.springframework.stereotype.Service;

@Service
public class AdminDashboardService {
	
	public AdminDashboard getDashboardInfo() {
		return AdminDashboard.builder()
				.carCount(120)
				.recentCarName("EV9, EV9 GT")
				.consultCount(45)
				.consultingCount(12)
				.dealerCount(15)
				.activeDealerCount(12)
				.promotionCount(3)
				.endingPromotionCount(1)
				.boardCount(10)
				.recentBoardTitle("차량 구매 관련 문의")
				.memberCount(320)
				.todayJoinCount(5)
				.build();
	}
}
