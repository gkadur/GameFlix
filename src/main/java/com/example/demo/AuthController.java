
package com.example.demo;

import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request) {

        if (request.getPassword().getBytes(
                java.nio.charset.StandardCharsets.UTF_8).length > 72) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Password is too long."));
        }

        User user = new User();
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setUsername(request.getUsername().trim());

        // Hash the password before saving it.
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        try {
            User savedUser = userRepository.saveAndFlush(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of(
                            "id", savedUser.getId(),
                            "email", savedUser.getEmail(),
                            "username", savedUser.getUsername()
                    )
            );

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("error", "Email is already registered.")
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request) {

        Optional<User> result = userRepository.findByEmail(
                request.getEmail().trim().toLowerCase());

        if (result.isEmpty() ||
                !passwordEncoder.matches(
                        request.getPassword(),
                        result.get().getPassword())) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password."));
        }

        User user = result.get();

        String token = jwtService.createToken(user);

        return ResponseEntity.ok(
                Map.of(
                        "token", token,
                        "tokenType", "Bearer",
                        "expiresIn", 1800
                )
        );
    }

}
