
package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(Authentication authentication) {

        String email = authentication.getName();

        Optional<User> result = userRepository.findByEmail(email);

        if (result.isEmpty()) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Unauthorized."));
        }

        User user = result.get();

        return ResponseEntity.ok(
                Map.of(
                        "id", user.getId(),
                        "email", user.getEmail(),
                        "username", user.getUsername(),
                        "createdAt", user.getCreatedAt().toString()
                )
        );
    }
}
