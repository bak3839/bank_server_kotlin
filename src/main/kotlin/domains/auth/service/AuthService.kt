package org.example.domains.auth.service

import org.example.common.exception.CustomException
import org.example.common.exception.ErrorCode
import org.example.common.jwt.JwtProvider
import org.example.common.logging.Logging
import org.example.interfaces.OAuthServiceInterface
import org.slf4j.Logger
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val oAuth2Services: Map<String, OAuthServiceInterface>,
    private val jwtProvider: JwtProvider,
    private val logger: Logger = Logging.getLogger(AuthService::class.java)
) {
    fun handleAuth(state: String, code: String): String = Logging.logFor(logger){ log ->
        val provider = state.lowercase()
        log["provider"] = provider

        val callService = oAuth2Services[provider] ?: throw CustomException(ErrorCode.PROVIDER_NOT_FOUND, provider)

        val accessToken = callService.getToken(code)
        val userInfo = callService.getUserInfo(accessToken.accessToken)
        val token = jwtProvider.createToken(provider, userInfo.email, userInfo.name, userInfo.id)

        // userInfo
        return@logFor ""
    }
}