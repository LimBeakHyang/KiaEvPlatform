package com.kiaev.admin.car;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/car")
public class AdminCarController {
	
	private final AdminCarService adminCarService;

    // 차량 목록
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("carList", adminCarService.findAll());
        return "admin/car/adminCarList";
    }

    // 차량 상세
    @GetMapping("/detail")
    public String detail(@RequestParam("carId") Long carId, Model model) {
        model.addAttribute("car", adminCarService.findById(carId));
        return "admin/car/adminCarDetail";
    }

    // 차량 등록 페이지
    @GetMapping("/register")
    public String registerPage() {
        return "admin/car/adminCarRegister";
    }

    // 차량 등록
    @PostMapping("/register")
    public String register(@ModelAttribute AdminCar adminCar,
                           @RequestParam("carFiles") MultipartFile[] carFiles) throws Exception {
        adminCarService.register(adminCar, carFiles);
        return "redirect:/admin/car/list";
    }

    // 차량 수정 페이지
    @GetMapping("/edit")
    public String editPage(@RequestParam("carId") Long carId, Model model) {
        model.addAttribute("car", adminCarService.findById(carId));
        return "admin/car/adminCarEdit";
    }

    // 차량 수정
    @PostMapping("/edit")
    public String edit(@ModelAttribute AdminCar adminCar,
                       @RequestParam(value = "mainFile", required = false) MultipartFile mainFile,
                       @RequestParam(value = "img1File", required = false) MultipartFile img1File,
                       @RequestParam(value = "img2File", required = false) MultipartFile img2File,
                       @RequestParam(value = "img3File", required = false) MultipartFile img3File) throws Exception {

        adminCarService.edit(adminCar, mainFile, img1File, img2File, img3File);
        return "redirect:/admin/car/detail?carId=" + adminCar.getCarId();
    }

    // 품절 처리
    @PostMapping("/soldout")
    public String soldOut(@RequestParam("carId") Long carId) {
        adminCarService.soldOut(carId);
        return "redirect:/admin/car/detail?carId=" + carId;
    }
}
