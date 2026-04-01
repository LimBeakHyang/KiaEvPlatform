package com.kiaev.client.car;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/car")
@RequiredArgsConstructor
public class CarController {
    
    private final CarService carService;

    // 차량 목록 페이지 조회
    @GetMapping("/list")
    public String carList(
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "carType", required = false) String carType,
        @RequestParam(value = "sort", required = false) String sort,
        Model model) {

        // 서비스에서 검색 및 필터링된 리스트 가져오기
        List<Car> cars = carService.searchCars(keyword, carType, sort);

        model.addAttribute("cars", cars);
        model.addAttribute("keyword", keyword);
        model.addAttribute("carType", carType);
        model.addAttribute("sort", sort);
        
        return "client/car/carList";
    }
    
    // 차량 상세 정보 페이지 조회
    // 경로: /car/detail/123 (PathVariable 방식)
    @GetMapping("/detail/{carNo}")
    public String carDetail(@PathVariable("carNo") Long carNo, Model model) {
        
        // 서비스에서 ID(PK)로 차량 1건 조회
        Car car = carService.findCarById(carNo);
        
        model.addAttribute("car", car);
        
        return "client/car/carDetail";
    }
}