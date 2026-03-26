package com.padmavatimedicals.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "forward:/index.html";
    }

    @GetMapping("/about")
    public String about() {
        return "forward:/about.html";
    }

    @GetMapping("/products")
    public String products() {
        return "forward:/products.html";
    }

    @GetMapping("/order")
    public String order() {
        return "forward:/order.html";
    }

    @GetMapping("/contact")
    public String contact() {
        return "forward:/contact.html";
    }
}
