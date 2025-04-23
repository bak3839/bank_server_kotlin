package org.example.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/*
    oauth2:
        providers:
            google:
                client-id: sample
                client-secret:
                redirect-url:
            kakao:
                client-id: sample
                client-secret:
                redirect-url:
 */

@Configuration
@ConfigurationProperties(prefix = "oauth")
class OAuth2Config {
    val providers: MutableMap<String, OAuth2ProviderValues> = mutableMapOf()
}

data class OAuth2ProviderValues(
    // client-id 스프링이 알아서 처리
    val clientId: String,
    val clientSecret: String,
    val redirectUrl: String
)