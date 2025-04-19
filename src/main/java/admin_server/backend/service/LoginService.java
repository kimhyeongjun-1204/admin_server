package admin_server.backend.service;

import admin_server.backend.dto.*;
import admin_server.backend.entity.Admin;
import admin_server.backend.repository.LoginRepository;
import admin_server.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /* 1. 로그인 처리 */
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        Admin admin = loginRepository.findByUsername(request.getUsername())
                .orElse(null);

        if (admin == null || !passwordEncoder.matches(request.getPassword(), admin.getHashedPassword())) {
            return ResponseEntity.ok(new LoginResponse(401, "")); // 인증 실패
        }

        String token = jwtUtil.generateToken(admin.getUsername());
        return ResponseEntity.ok(new LoginResponse(201, token)); // 성공
    }

    /* 2. 회원가입 처리 */
    public ResponseEntity<SignupResponse> signup(SignupRequest request) {
        // 1. 아이디 중복 체크
        if (loginRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.ok(new SignupResponse(409)); // 중복된 아이디
        }

        // 2. 새로운 관리자 생성
        Admin admin = new Admin();
        admin.setUsername(request.getUsername());
        admin.setHashedPassword(passwordEncoder.encode(request.getPassword()));
        admin.setName(request.getName());
        admin.setBirth(request.getBirth());
        
        // 3. 관리자 ID 생성 (마지막 ID + 1)
        Integer lastAdminId = loginRepository.findTopByOrderByAdminIdDesc()
                .map(Admin::getAdminId)
                .orElse(0);
        admin.setAdminId(lastAdminId + 1);

        // 4. 저장
        try {
            loginRepository.save(admin);
            System.out.println("Admin saved successfully");
            return ResponseEntity.ok(new SignupResponse(201)); // 성공
        } catch (Exception e) {
            System.err.println("Error saving admin: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(new SignupResponse(500)); // 서버 오류
        }
    }

    /* 3. 관리자 목록 조회 */
    public ResponseEntity<AdminListResponse> getAdminList() {
        try {
            List<Admin> allAdmins = loginRepository.findAll();
            
            // signup_date가 null이 아닌 경우만 승인된 관리자로 분류
            List<Admin> approvedAdmins = allAdmins.stream()
                .filter(admin -> admin.getSignupDate() != null)
                .collect(Collectors.toList());
            
            // signup_date가 null인 경우만 대기 중인 관리자로 분류
            List<Admin> pendingAdmins = allAdmins.stream()
                .filter(admin -> admin.getSignupDate() == null)
                .collect(Collectors.toList());

            return ResponseEntity.ok(new AdminListResponse(200, approvedAdmins, pendingAdmins));
        } catch (Exception e) {
            System.err.println("Error fetching admin list: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(new AdminListResponse(500, null, null));
        }
    }

    /* 4. 관리자 승인 처리 */
    public ResponseEntity<SignupResponse> approveAdmin(String username) {
        try {
            Admin admin = loginRepository.findByUsername(username)
                    .orElse(null);

            if (admin == null) {
                return ResponseEntity.ok(new SignupResponse(404)); // 사용자를 찾을 수 없음
            }

            // 승인 날짜 설정
            admin.setSignupDate(LocalDate.now());
            loginRepository.save(admin);

            return ResponseEntity.ok(new SignupResponse(200)); // 성공
        } catch (Exception e) {
            System.err.println("Error approving admin: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(new SignupResponse(500)); // 서버 오류
        }
    }
} 