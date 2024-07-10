package com.dhananjai.crm.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
    	
    	// Get the request URI that caused the error
        String requestURI = (String) request.getAttribute("javax.servlet.error.request_uri");
        model.addAttribute("requestURI", requestURI);

        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        model.addAttribute("statusCode", statusCode);
        return "error";
    }

    public String getErrorPath() {
        return "/error";
    }
}
