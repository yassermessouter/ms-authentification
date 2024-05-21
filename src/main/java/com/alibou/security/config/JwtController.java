package com.alibou.security.config;

import com.alibou.security.token.Token;
import com.alibou.security.token.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // Allow requests from http://localhost:3000


public class JwtController {
    private final TokenRepository tokenRepository;

    @PostMapping("/verify-token")
    public JwtDto verifyToken(@RequestParam String token) {
        JwtDto jwtDto = JwtDto.builder()
                .isValid(false) // corrected spelling
                .userId(0)
                .build();
        Optional<Token> jwt = tokenRepository.findByToken(token);
        if(jwt.isPresent()){
            if (!jwt.get().isExpired() && !jwt.get().isRevoked()) {
                jwtDto.setIsValid(true);
                jwtDto.setUserId(jwt.get().getUser().getId());
                return jwtDto;
            }
        }
        return jwtDto;


    }


}
