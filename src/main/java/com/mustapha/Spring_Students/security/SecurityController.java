package com.mustapha.Spring_Students.security;

import com.mustapha.Spring_Students.security.entities.ResetPasswordRequest;
import com.mustapha.Spring_Students.security.service.AccountService;
import com.mustapha.Spring_Students.service.CaptchaService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class SecurityController {
    public AuthenticationManager authenticationManager;
    public JwtEncoder jwtEncoder;
    private AccountService accountService;
    private CaptchaService captchaService;
    @GetMapping("/profile")
    public Authentication authentication( Authentication authentication ){
        return authentication;
    }
    @PostMapping("/login")
    public Map<String,String> login(String username, String password , String captcha){
                if (!captchaService.verifyCaptcha(captcha)) {
                    return Map.of();
                }
        Authentication authentication=  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        Instant instant = Instant.now();
        String scope=  authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
        JwtClaimsSet jwtClaimsSet=JwtClaimsSet.builder()
                .issuedAt(instant)
                .expiresAt(instant.plus(30, ChronoUnit.MINUTES))
                .subject(username)
                .claim("scope",scope)
                .build();
        JwtEncoderParameters jwtEncoderParameters=JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS512).build(),jwtClaimsSet
        );
        String jwt =jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
        return Map.of("access-token",jwt);
    }



        @PostMapping("/forgot-password")
        public Boolean forgotPassword(@RequestBody Map<String, String> request) {
            String email = request.get("email");
            return accountService.generatePasswordResetToken(email);
        }

        @PostMapping("/reset-password")
        public Boolean resetPassword(@RequestBody ResetPasswordRequest resetRequest) {
          Boolean reset;
          reset=  accountService.resetPassword(resetRequest.getToken(), resetRequest.getNewPassword());
            System.out.println("password "+reset);
            return reset;
        }

}
