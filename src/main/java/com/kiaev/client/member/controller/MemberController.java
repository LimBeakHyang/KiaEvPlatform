package com.kiaev.client.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kiaev.client.member.domain.Member;
import com.kiaev.client.member.service.MemberService;

@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;

	@GetMapping("/member/join")
	public String joinForm() {
		return "client/member/joinForm";
	}

	@PostMapping("/member/join")
	public String joinSubmit(@RequestParam("loginId") String loginId, @RequestParam("memberPw") String memberPw,
			@RequestParam("memberPwCheck") String memberPwCheck, @RequestParam("memberName") String memberName,
			@RequestParam("birthDate") String birthDate, @RequestParam("email") String email,
			@RequestParam("phone") String phone, @RequestParam(value = "zipcode", required = false) String zipcode,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "detailAddress", required = false) String detailAddress) {
		if (!memberPw.equals(memberPwCheck)) {
			return "client/member/joinForm";
		}

		Member member = new Member();
		member.setLoginId(loginId);
		member.setMemberPw(memberPw);
		member.setMemberName(memberName);
		member.setBirthDate(birthDate);
		member.setEmail(email);
		member.setPhone(phone);
		member.setZipcode(zipcode);
		member.setAddress(address);
		member.setDetailAddress(detailAddress);

		memberService.save(member);

		return "redirect:/login?joinSuccess=true";
	}

	@GetMapping("/member/check-loginId")
	@ResponseBody
	public Map<String, Boolean> checkLoginId(@RequestParam("loginId") String loginId) {
		boolean exists = memberService.existsByLoginId(loginId);
		Map<String, Boolean> result = new HashMap<>();
		result.put("exists", exists);
		return result;
	}

	@GetMapping("/member/check-email")
	@ResponseBody
	public Map<String, Boolean> checkEmail(@RequestParam("email") String email) {
		boolean exists = memberService.existsByEmail(email);
		Map<String, Boolean> result = new HashMap<>();
		result.put("exists", exists);
		return result;
	}
}