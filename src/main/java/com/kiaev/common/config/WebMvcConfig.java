package com.kiaev.common.config;

import com.kiaev.common.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                // 1. 감시할 주소 (게시판과 회원 페이지 전체)
                .addPathPatterns("/board/**", "/member/**") 
                // 2. 제외할 주소 (로그인 없이도 통과해야 하는 길)
                .excludePathPatterns(   // 아래 경로들은 로그인 없이도 통과!
                        "/", 
                        "/login", 
                        "/logout", 
                        "/member/join", 
                        "/member/terms",
                        "/member/checkId",    // 아이디 중복확인 허용
                        "/member/checkEmail", // 이메일 중복확인 허용 (여기가 포인트!)
                        "/css/**", 
                        "/js/**", 
                        "/img/**"
                    );
    }
}