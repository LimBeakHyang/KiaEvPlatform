package com.kiaev.client.consult;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.kiaev.client.login.Login;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/consult")
@RequiredArgsConstructor
public class ConsultController {

    private final ConsultService consultationService;

    // 상담 신청 페이지 (/consult/form)
    @ResponseBody
    @GetMapping("/form")
    public String showForm(@RequestParam(value = "carName", required = false) String carName, 
                           HttpSession session) {

        if (session.getAttribute("loginUser") == null) {
            return "<script>"
                    + "alert('로그인 후에 이용 가능한 서비스 입니다.');"
                    + "location.href='/login';"
                    + "</script>";
        }

        // 로그인 성공 시 carName 파라미터가 있다면 URL에 붙여서 보냄
        String redirectUrl = "/consult/formPage";
        if (carName != null && !carName.isEmpty()) {
            redirectUrl += "?carName=" + carName;
        }

        return "<script>"
                + "location.href='" + redirectUrl + "';"
                + "</script>";
    }

    // 실제 상담 신청 폼 페이지
    @GetMapping("/formPage")
    public String formPage(@RequestParam(value = "carName", required = false) String carName, 
                           HttpSession session, 
                           Model model) {

        if (session.getAttribute("loginUser") == null) {
            return "redirect:/consult/form";
        }

        // 넘어온 차량 이름을 모델에 담아 전송 (기존 코드에 carName 처리 추가)
        model.addAttribute("selectedCar", carName);

        return "client/consult/consultForm";
    }

    // 상담 등록 (기존 로직 및 로그 출력 그대로 유지)
    @PostMapping("/register")
    public String submitForm(Consult consultation, HttpSession session) {

        System.out.println("=== submitForm 진입 ===");

        Object loginUserObj = session.getAttribute("loginUser");

        if (loginUserObj == null) {
            System.out.println("비로그인 상태 -> /consult/form");
            return "redirect:/consult/form";
        }

        if (!(loginUserObj instanceof Login)) {
            System.out.println("loginUser 타입 이상 -> /consult/form");
            return "redirect:/consult/form";
        }

        Login loginUser = (Login) loginUserObj;

        System.out.println("loginUser = " + loginUser);
        System.out.println("memberNo(loginUser) = " + loginUser.getMemberNo());
        System.out.println("memberStatus(loginUser) = " + loginUser.getMemberStatus());
        System.out.println("consultation.carNo = " + consultation.getCarNo());
        System.out.println("consultation.mainRangeKm = " + consultation.getMainRangeKm());
        System.out.println("consultation.usePurpose = " + consultation.getUsePurpose());
        System.out.println("consultation.budgetAmount = " + consultation.getBudgetAmount());
        System.out.println("consultation.fellowData = " + consultation.getFellowData());
        System.out.println("consultation.consultContent = " + consultation.getConsultContent());

        if (loginUser.getMemberNo() == null) {
            System.out.println("memberNo 없음 -> /consult/form");
            return "redirect:/consult/form";
        }

        consultation.setMemberNo(loginUser.getMemberNo());

        if (consultation.getConsultStatus() == null || consultation.getConsultStatus().isBlank()) {
            consultation.setConsultStatus("대기");
        }

        Consult savedConsult = consultationService.save(consultation);

        System.out.println("저장 완료!");
        System.out.println("savedConsult.consultNo = " + savedConsult.getConsultNo());
        System.out.println("savedConsult.memberNo = " + savedConsult.getMemberNo());
        System.out.println("savedConsult.carNo = " + savedConsult.getCarNo());
        System.out.println("savedConsult.consultStatus = " + savedConsult.getConsultStatus());
        System.out.println("consult 전체 개수 = " + consultationService.findAll().size());
        System.out.println("-> /consult/success 이동");

        return "redirect:/consult/success";
    }

    // 상담 신청 완료 페이지
    @GetMapping("/success")
    public String successPage(HttpSession session) {

        if (session.getAttribute("loginUser") == null) {
            return "redirect:/consult/form";
        }

        return "client/consult/success";
    }

    // 상담 상세 조회
    @GetMapping("/detail")
    public String consultDetail(@RequestParam("id") Long id, Model model, HttpSession session) {

        Object loginUserObj = session.getAttribute("loginUser");

        if (loginUserObj == null) {
            return "redirect:/consult/form";
        }

        if (!(loginUserObj instanceof Login)) {
            return "redirect:/consult/form";
        }

        Login loginUser = (Login) loginUserObj;

        Long memberNo = loginUser.getMemberNo();
        String memberStatus = loginUser.getMemberStatus();

        if (!isAdminOrDealer(memberStatus) && memberNo == null) {
            return "redirect:/consult/form";
        }

        Consult consult;

        if (isAdminOrDealer(memberStatus)) {
            consult = consultationService.findById(id);
        } else {
            consult = consultationService.findByConsultNoAndMemberNo(id, memberNo);
        }

        if (consult == null) {
            return "redirect:/consult/form";
        }

        model.addAttribute("consult", consult);
        model.addAttribute("member", loginUser);
        model.addAttribute("memberStatus", memberStatus);

        return "client/consult/consultDetail";
    }

    // 관리자/딜러 권한 체크
    private boolean isAdminOrDealer(String memberStatus) {
        if (memberStatus == null) {
            return false;
        }

        return memberStatus.equalsIgnoreCase("ADMIN")
                || memberStatus.equalsIgnoreCase("DEALER")
                || memberStatus.equals("관리자")
                || memberStatus.equals("딜러");
    }
}