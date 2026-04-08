package com.kiaev.client.consult;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/consult")
@RequiredArgsConstructor
public class ClientConsultController {

    private final ClientConsultService consultationService;

    // 상담 신청 페이지 (로그인 체크)
    @ResponseBody
    @GetMapping("/form")
    public String showForm(HttpSession session) {

        if (session.getAttribute("loginUser") == null) {
            return "<script>"
                    + "alert('로그인이 필요한 서비스입니다.');"
                    + "location.href='/login';"
                    + "</script>";
        }

        return "<script>location.href='/consult/formPage';</script>";
    }

    // 실제 상담 신청 폼 페이지
    @GetMapping("/formPage")
    public String formPage(HttpSession session) {

        if (session.getAttribute("loginUser") == null) {
            return "redirect:/consult/form";
        }

        return "client/consult/consultForm";
    }

    // 상담 등록
    @PostMapping("/register")
    public String submitForm(ClientConsult consultation, HttpSession session) {

        System.out.println("=== submitForm 진입 ===");
        System.out.println("loginUser = " + session.getAttribute("loginUser"));
        System.out.println("memberNo = " + session.getAttribute("memberNo"));

        if (session.getAttribute("loginUser") == null) {
            System.out.println("로그인 안됨 -> /consult/form");
            return "redirect:/consult/form";
        }

        Object memberNoObj = session.getAttribute("memberNo");

        if (memberNoObj instanceof Long) {
            consultation.setMemberNo((Long) memberNoObj);
        } else if (memberNoObj instanceof Integer) {
            consultation.setMemberNo(((Integer) memberNoObj).longValue());
        } else {
            System.out.println("memberNo 없음 또는 타입 다름 -> /consult/form");
            return "redirect:/consult/form";
        }

        if (consultation.getConsultStatus() == null || consultation.getConsultStatus().isBlank()) {
            consultation.setConsultStatus("대기");
        }

        consultationService.save(consultation);
        System.out.println("저장 완료 -> /consult/success");

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

        if (session.getAttribute("loginUser") == null) {
            return "redirect:/consult/form";
        }

        Object memberNoObj = session.getAttribute("memberNo");
        Object memberStatusObj = session.getAttribute("memberStatus");

        Long memberNo = null;
        String memberStatus = null;

        if (memberNoObj instanceof Long) {
            memberNo = (Long) memberNoObj;
        } else if (memberNoObj instanceof Integer) {
            memberNo = ((Integer) memberNoObj).longValue();
        }

        if (memberStatusObj != null) {
            memberStatus = memberStatusObj.toString();
        }

        if (!isAdminOrDealer(memberStatus) && memberNo == null) {
            return "redirect:/consult/form";
        }

        ClientConsult consult;

        // 관리자/딜러는 전체 조회 가능
        if (isAdminOrDealer(memberStatus)) {
            consult = consultationService.findById(id);
        } else {
            // 일반 회원은 본인 상담만 조회 가능
            consult = consultationService.findByConsultNoAndMemberNo(id, memberNo);
        }

        if (consult == null) {
            return "redirect:/consult/form";
        }

        model.addAttribute("consult", consult);
        model.addAttribute("member", session.getAttribute("loginUser"));
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