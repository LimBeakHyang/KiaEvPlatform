package com.kiaev.admin.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboard {
	private long carCount;
	private String recentCarName;
	
	private long consultCount;
	private long consultingCount;
	
	private long dealerCount;
	private long activeDealerCount;
	
	private long promotionCount;
	private long endingPromotionCount;
	
	private long boardCount;
	private String recentBoardTitle;
	
	private long memberCount;
	private long todayJoinCount;
}
