package com.example.vectorstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RouteController {

    @GetMapping("/korea")
    public String korea(){
        return "rag";
    }
}
