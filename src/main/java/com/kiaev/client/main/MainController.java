package com.kiaev.client.main;

import com.kiaev.client.car.Car;
import com.kiaev.client.car.CarService;
import com.kiaev.client.promotion.Promotion;
import com.kiaev.client.promotion.PromotionRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final CarService carService;
    private final PromotionRepository promotionRepository; // 추가된 레포지토리

    @GetMapping("/main")
    public String mainPage(@RequestParam(value = "lang", required = false) String lang, 
                           HttpSession session, Model model) { 
        
        // 1-1. 언어 세션 처리
        if (lang != null) {
            session.setAttribute("lang", lang.toLowerCase());
        }

        String currentLang = (String) session.getAttribute("lang");
        if (currentLang == null) {
            currentLang = "kr";
        }

        // 1-2. [핵심] Ray EV, EV6, EV9 모델만 필터링해서 가져오기
        List<Car> allCars = carService.findAll(); 
        List<Car> bestLineup = allCars.stream()
                .filter(car -> List.of("Ray EV", "EV6", "EV9").contains(car.getModelName())) 
                .sorted(Comparator.comparing(Car::getPrice)) 
                .collect(Collectors.toList());

        model.addAttribute("carLineup", bestLineup);

        // 1-3. [추가] 스토리보드 요구사항: 프로모션 최신순 최대 3개 추출
        List<Promotion> promoList = promotionRepository.findAllByOrderByIdDesc()
                .stream()
                .filter(p -> p.isActive() && 
                             (p.getEndDate().isAfter(LocalDateTime.now()) || p.getEndDate().isEqual(LocalDateTime.now())))
                .limit(3)
                .collect(Collectors.toList());
        
        model.addAttribute("promoList", promoList);

        // EN 모드일 경우 영어 카탈로그 전용 페이지 반환
        if ("en".equals(currentLang)) {
            return "common/englishCatalog";
        }

        return "client/main/main"; 
    }
    
    /*
    @GetMapping("/consult/form")
    public String consultForm() { 
        return "client/consult/consultForm"; 
    }
    */

    /*
    @GetMapping("/recommend/main")
    public String recommendMain() { 
        return "client/recommend/recommendMain"; 
    }
    */
    
    // 팝업 창
    @GetMapping("/popup")
    public String showPopup() {
        return "client/main/popup";
    }

    // 7. 로그인 페이지
    //@GetMapping("/login")
    //public String loginForm() {
       // return "client/login/loginForm";
    }

