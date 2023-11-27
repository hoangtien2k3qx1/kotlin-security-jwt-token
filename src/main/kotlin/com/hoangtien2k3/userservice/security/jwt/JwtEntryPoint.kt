package com.hoangtien2k3.userservice.security.jwt

import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class JwtEntryPoint : AuthenticationEntryPoint{
    private val logger = LoggerFactory.getLogger(JwtEntryPoint::class.java)

    // bắt lỗi Exception
    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        logger.error("Unauthenticated error Message {}", authException.message)
        response.sendError(HttpServletResponse.SC_ACCEPTED, "Error -> Unauthenticated")
    }
}