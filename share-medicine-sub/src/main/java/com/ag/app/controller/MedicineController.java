package com.ag.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MedicineController {

    @GetMapping("/health")
    public String sayHello(){
        return "I am up & running";
    }
}
