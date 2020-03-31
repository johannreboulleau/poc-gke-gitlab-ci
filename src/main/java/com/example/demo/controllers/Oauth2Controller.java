package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Oauth2Controller {

    @GetMapping("oauth")
    public String getOauthPage(Model model) {
        model.addAttribute("name", "Johann");
        return "oauth";
    }
}
