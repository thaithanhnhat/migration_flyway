package com.redis.redis_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.redis.redis_demo.service.MailService;
import com.redis.redis_demo.utils.OtpGenerator;

@RestController
public class MailController {

    @Autowired
    private MailService emailService;

    @GetMapping("/sendOtp")
    public String sendOtp(@RequestParam String email) {
        String otp = OtpGenerator.generateOtp();
        emailService.sendOtpEmail(email, otp);
        return "OTP sent to " + email;
    }
}
