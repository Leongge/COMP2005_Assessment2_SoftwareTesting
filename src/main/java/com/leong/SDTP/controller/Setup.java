package com.leong.SDTP.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Setup {
    @GetMapping("/")
    public String hello(){
        return "Hello World";
    }
}