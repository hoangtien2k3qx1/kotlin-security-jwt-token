package com.hoangtien2k3.userservice.security.jwt

import com.hoangtien2k3.userservice.security.userprinciple.UserDetailService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

class JwtTokenFilter : OncePerRequestFilter() {
    private val logger = LoggerFactory.getLogger(JwtTokenFilter::class.java)

    @Autowired
    private val jwtProvider: JwtProvider? = null

    @Autowired
    private val userDetailService: UserDetailService? = null

    // tìm token trong request
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val token = getJwt(request) // lấy ra token trong request

            // Or secretkey
            if (token != null && jwtProvider!!.validateToken(token)) {
                val username = jwtProvider.getUserNameFromToken(token)
                val userDetails: UserDetails = userDetailService!!.loadUserByUsername(username)
                val authenticationToken = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.authorities
                )
                authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authenticationToken

                // Tạo mới refresh token
                val refreshToken = jwtProvider.createRefreshToken(authenticationToken)

                // Gửi cả token và refresh token về cho người dùng
                response.setHeader("Authorization", "Bearer $token")
                response.setHeader("Refresh-Token", refreshToken)
            }
        } catch (e: Exception) {
            logger.error("Can't set user authentication -> Message: ", e)
        }
        filterChain.doFilter(request, response)
    }

    private fun getJwt(request: HttpServletRequest): String? {
        val authHeader: String = request.getHeader("Authorization")
        return if (authHeader.startsWith("Bearer")) {
            authHeader.replace("Bearer", "")
        } else null
    }
}