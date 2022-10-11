package com.shivu.bookhouseapp.home;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController{
    
    @RequestMapping("/error")
    public String handleError(HttpServletRequest hRequest, Model model){
        String errorPage = "error";
        model.addAttribute("message", hRequest.getAttribute(RequestDispatcher.ERROR_MESSAGE));        
        return errorPage;
    }
}
