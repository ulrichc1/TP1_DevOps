/**
 * INGE2APP LSI1
 * Date : 06/03/2026
 */
package com.ulrich.dockerjavaspringboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AccueilController {

    @GetMapping("/")
    public String accueil() {
        return "redirect:/produits";
    }

    @GetMapping("/sayHello")
    @ResponseBody
    public String sayHello() {
        return "Hello";
    }
}
