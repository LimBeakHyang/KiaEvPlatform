package com.kiaev.admin.dealer;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/dealer")
public class AdminDealerController {
	private final AdminDealerService adminDealerService;

    public AdminDealerController(AdminDealerService adminDealerService) {
        this.adminDealerService = adminDealerService;
    }
    
    // 딜러 목록
    @GetMapping("/list")
    public String dealerList(
    		@RequestParam(required = false) String branchName,
            @RequestParam(required = false) String accountStatus,
            Model model) {
    	List<AdminDealer> dealerList;
    	
    	boolean hasBranch = branchName != null && !branchName.isBlank() && !"전체".equals(branchName);
    	boolean hasStatus = accountStatus != null && !accountStatus.isBlank() && !"전체".equals(accountStatus);
    	
    	if (hasBranch && hasStatus) {
            dealerList = adminDealerService.findDealersByBranchAndStatus(branchName, accountStatus);
        } else if (hasBranch) {
            dealerList = adminDealerService.findDealersByBranchName(branchName);
        } else if (hasStatus) {
            dealerList = adminDealerService.findDealersByAccountStatus(accountStatus);
        } else {
            dealerList = adminDealerService.findAllDealers();
        }
    	
    	model.addAttribute("dealerList", dealerList);
        model.addAttribute("selectedBranchName", branchName);
        model.addAttribute("selectedAccountStatus", accountStatus);

        return "admin/dealer/adminDealerList";
    }
    
    // 딜러 상세
    @GetMapping("/detail/{dealerId}")
    public String dealerDetail(@PathVariable Long dealerId, Model model) {
        AdminDealer dealer = adminDealerService.findDealerById(dealerId);
        model.addAttribute("dealer", dealer);
        return "admin/dealer/adminDealerDetail";
    }
    
    // 신규 딜러 등록 화면
    @GetMapping("/register")
    public String dealerRegisterForm(Model model) {
        model.addAttribute("dealer", new AdminDealer());
        return "admin/dealer/adminDealerRegister";
    }
    
    // 신규 딜러 등록 처리
    @PostMapping("/register")
    public String dealerRegister(AdminDealer adminDealer) {
        adminDealerService.registerDealer(adminDealer);
        return "redirect:/admin/dealer/list";
    }
}
