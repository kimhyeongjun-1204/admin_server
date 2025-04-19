package admin_server.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import admin_server.backend.dto.LoginRequest;
import admin_server.backend.dto.LoginResponse;
import admin_server.backend.dto.SignupRequest;
import admin_server.backend.dto.SignupResponse;
import admin_server.backend.service.LoginService;

@RestController
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /* 1. 로그인 요청 */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        System.out.println(request.getPassword() + " " + request.getUsername());
        return loginService.login(request);
    }

    /* 2. 회원가입 요청 */
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
        return loginService.signup(request);
    }
} 