package com.charan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@Controller
public class AkitaController {
    @PostMapping("/akita/sendemails")
    public String sendEmails(@RequestBody Map<String, String> input) {
        if (input == null) {
            return "input empty";
        }
        Set<String> keys = input.keySet();

        for (String key : keys) {
            System.out.println(key + ": " + input.get(key));
        }
        return "done";
    }
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
