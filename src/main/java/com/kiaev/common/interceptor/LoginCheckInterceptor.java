package com.kiaev.common.interceptor;

// 모두 jakarta로 통일하여 깔끔하게 정리!
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        // 1. 현재 요청의 세션을 가져옵니다.
        HttpSession session = request.getSession();

        // 2. 세션에 로그인 정보(예: "member")가 있는지 확인합니다.
        if (session.getAttribute("loginUser") == null) {
            
        	// [추가] 사용자가 원래 가려던 주소(URI)를 세션에 "prevPage"라는 이름으로 저장합니다.
            String uri = request.getRequestURI(); 
            session.setAttribute("prevPage", uri);
        	
            // 3. 로그인 정보가 없다면, 로그인 페이지로 리다이렉트 시킵니다.
            response.sendRedirect("/login"); 
            
            // 4. false를 반환하면 원래 가려던 Controller(게시판)로 가지 않고 멈춥니다.
            return false;
        }

        // 5. 로그인 정보가 있다면 true를 반환하여 정상적으로 Controller로 진입합니다.
        return true;
    }
}