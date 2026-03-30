package com.kiaev.client.main;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/main")
    public String mainPage(@RequestParam(value = "lang", required = false) String lang, 
                           HttpSession session) { 
        
        if (lang != null) {
            session.setAttribute("lang", lang.toLowerCase());
        }

        String currentLang = (String) session.getAttribute("lang");
        if (currentLang == null) {
            currentLang = "kr";
        }

        if ("en".equals(currentLang)) {
            return "client/main/englishCatalog";
        }

        return "client/main/main"; 
    }

    @GetMapping("/popup")
    public String showPopup() {
        return "client/main/popup";
    }
}