package com.alibou.security.config;

import com.alibou.security.token.Token;
import com.alibou.security.token.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class JwtController {
    private final TokenRepository tokenRepository;

    @PostMapping("/verify-token")
    public JwtDto verifyToken(@RequestParam String token) {
        JwtDto jwtDto = JwtDto.builder()
                .isValid(false) // corrected spelling
                .userId(0)
                .companyId(0)
                .build();

        Optional<Token> jwt = tokenRepository.findByToken(token);
        if(jwt.isPresent()){
            if (!jwt.get().isExpired() && !jwt.get().isRevoked()) {
                jwtDto.setIsValid(true);
                jwtDto.setUserId(jwt.get().getUser().getId());
                jwtDto.setCompanyId(jwt.get().getUser().getCompany().getId());
                return jwtDto;
            }
        }
        return jwtDto;
    }


}
