package com.document.docuquery.controller;

import com.document.docuquery.dto.JwtResponse;
import com.document.docuquery.dto.LoginRequest;
import com.document.docuquery.dto.RegisterRequest;
import com.document.docuquery.entity.Role;
import com.document.docuquery.entity.User;
import com.document.docuquery.repository.RoleRepository;
import com.document.docuquery.repository.UserRepository;
import com.document.docuquery.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder encoder;
    @Autowired private AuthenticationManager authManager;
    @Autowired private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.userName).isPresent()) {
            return ResponseEntity.badRequest().body("Username is taken");
        }

        Set<Role> roles = new HashSet<>();
        for (String roleStr : request.roles) {
            Role role = roleRepository.findByName("ROLE_" + roleStr.toUpperCase()).orElseThrow();
            roles.add(role);
        }

        User user = new User();
        user.setUsername(request.userName);
        user.setPassword(encoder.encode(request.password));
        user.setRoles(roles);

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.userName, request.password));

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(token, request.userName, roles));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // Just inform client to delete token; or implement blacklist if needed.
        return ResponseEntity.ok("Logout successful. Please remove the token from client side.");
    }
}


