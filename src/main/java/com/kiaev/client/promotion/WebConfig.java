package com.kiaev.client.promotion;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 브라우저에 http://localhost:8080/uploads/... 라고 입력이 들어오면
        registry.addResourceHandler("/uploads/**")
                // 윈도우의 C드라이브 아래에 있는 uploads 폴더에서 파일을 찾아라!
                // 주의: 윈도우 경로를 적을 때는 file:/// 다음에 C:/ 를 적어줍니다.
                .addResourceLocations("file:///C:/uploads/"); 
    }
}