package com.kiaev.client.main;

import com.kiaev.client.car.Car;
import com.kiaev.client.car.CarService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// 메인 및 공통 페이지 컨트롤러
@Controller
@RequiredArgsConstructor
public class MainController {

    private final CarService carService;

    // 1. 메인 페이지 (언어 전환 및 베스트 라인업 3종 추출)
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
                .sorted(Comparator.comparing(Car::getPrice)) // 가격 낮은 순 정렬
                .collect(Collectors.toList());

        // 뷰(HTML)로 베스트 라인업 데이터 전달
        model.addAttribute("carLineup", bestLineup);

        // 1-3. EN 모드일 경우 영어 카탈로그 전용 페이지 반환
        if ("en".equals(currentLang)) {
            return "common/englishCatalog";
        }

        // KR 모드일 경우 기존 메인 페이지 반환
        return "client/main/main"; 
    }

    // 2. 상담 신청 페이지
    @GetMapping("/consult/form")
    public String consultForm() { 
        return "client/consult/consultForm"; 
    }

    /*
    // 3. 문의 게시판 목록
    @GetMapping("/board/list")
    public String boardList() { 
        return "client/board/boardList"; 
    }
    */
    
    /*
    // 4. 프로모션 목록
    @GetMapping("/promotion/list")
    public String promotionList() { 
        return "client/promotion/promotionList"; 
    }
    */

    // 5. 충전소 확인 및 추천 메인
    @GetMapping("/recommend/main")
    public String recommendMain() { 
        return "client/recommend/recommendMain"; 
    }
    
    // 6. 메인 팝업창
    @GetMapping("/popup")
    public String showPopup() {
        return "client/main/popup";
    }
    
    // 7. 로그인 페이지
    @GetMapping("/login")
    public String loginForm() {
        return "client/login/loginForm";
    }
}