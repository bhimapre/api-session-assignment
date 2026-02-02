package com.example.apiassignment.controllers;

import com.example.apiassignment.exception.BadRequestException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletResponse response) {

        if (!"admin".equals(username) || !"admin123".equals(password)) {
            throw new BadRequestException("Invalid credentials");
        }

        String token = "DUMMY_AUTH_TOKEN_123";

        Cookie cookie = new Cookie("AUTH_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);

        Map<String, String> res = new HashMap<>();
        res.put("message", "Login successful");

        return ResponseEntity.ok(res);
    }

}
