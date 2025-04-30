package org.example.security.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.example.common.exception.ErrorCode
import org.example.common.jwt.JwtProvider
import org.example.types.dto.ResponseProvider
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
    private val jwtProvider: JwtProvider,
    private val pathMatcher: AntPathMatcher
): OncePerRequestFilter() {
    private val JWT_AUTH_ENDPOINT = arrayOf(
        "api/v1/bank/**",
        "api/v1/history/**",
    )

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestURI = request.requestURI

        if(shouldPerformAuthentication(requestURI)) {

        } else {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json"

            val errResponse = ResponseProvider.failed(
                code = HttpStatus.UNAUTHORIZED,
                message = ErrorCode.ACCESS_TOKEN_NEED.message,
                null
            )

            response.writer.write(ObjectMapper().writeValueAsString(errResponse))
            response.writer.flush()
        }
    }

    private fun shouldPerformAuthentication(uri: String): Boolean {
        for(endPoint in JWT_AUTH_ENDPOINT) {
            // 걸리면 검증이 필요한 URI
            if(pathMatcher.match(endPoint, uri)) {
                return true
            }
        }

        return false
    }
}