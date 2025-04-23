package org.example.domains.auth.service

import org.example.common.exception.CustomException
import org.example.common.exception.ErrorCode
import org.example.config.OAuth2Config
import org.example.interfaces.OAuth2TokenResponse
import org.example.interfaces.OAuth2UserResponse
import org.example.interfaces.OAuthServiceInterface
import org.springframework.stereotype.Service

private const val key = "google"

@Service(key)
class GoogleAuthService(
    private val config: OAuth2Config
) : OAuthServiceInterface{
    private val oAuthInfo = config.providers[key] ?:
    throw CustomException(ErrorCode.AUTH_CONFIG_NOT_FOUND, key)

    override val providerName: String = key

    override fun getToken(code: String): OAuth2TokenResponse {
        TODO("Not yet implemented")
    }

    override fun getUserInfo(accessToken: String): OAuth2UserResponse {
        TODO("Not yet implemented")
    }
}