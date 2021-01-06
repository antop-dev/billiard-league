package org.antop.billiardslove.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antop.billiardslove.config.properties.GoogleProperties;
import org.antop.billiardslove.config.properties.KakaoProperties;
import org.antop.billiardslove.util.Aes256;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 초기화 API
 *
 * @author antop
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class InitApi {
    private final KakaoProperties kakaoProperties;
    private final GoogleProperties googleProperties;

    @PostMapping("api/v1/init")
    public InitResponse init() {
        String secretKey = Aes256.generateKey();

        return InitResponse.builder()
                .secretKey(secretKey)
                .kakaoKey(Aes256.encrypt(kakaoProperties.getJavaScriptKey(), secretKey))
                .adSenseKey(Aes256.encrypt(googleProperties.getAdSenseKey(), secretKey))
                .build();
    }

}