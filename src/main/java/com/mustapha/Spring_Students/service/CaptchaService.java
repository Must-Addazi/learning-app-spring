package com.mustapha.Spring_Students.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CaptchaService {
    @Value("${RECAPTCHA_SECRET}")
    private String secretKey;

    @Value("${recaptcha.url}")
    private String recaptchaUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean verifyCaptcha(String captchaResponse) {
        String url = recaptchaUrl + "?secret=" + secretKey + "&response=" + captchaResponse;

        Map<String, Object> response = restTemplate.postForObject(url, null, Map.class);

        // Vérifier si Google a validé le token
        return response != null && (Boolean) response.get("success");
    }
}
