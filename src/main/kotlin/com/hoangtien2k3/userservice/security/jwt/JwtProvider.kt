package com.hoangtien2k3.userservice.security.jwt

import com.hoangtien2k3.userservice.security.userprinciple.UserPrinciple
import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.Collectors

@Component
class JwtProvider {
    private val logger = LoggerFactory.getLogger(JwtProvider::class.java)

    @Value("\${jwt.secret}")
    private val jwtSecret: String? = null

    @Value("\${jwt.expiration}")
    private val jwtExpiration = 0

    @Value("\${jwt.refreshExpiration}")
    private val jwtRefreshExpiration = 0

    fun createToken(authentication: Authentication): String {
        val userPrinciple: UserPrinciple = authentication.principal as UserPrinciple

        // danh sách các quyền (authorities) của người dùng
        val authorities: List<String> = userPrinciple.authorities!!.stream()
            .map {
                obj: GrantedAuthority -> obj.authority
            }
            .collect(Collectors.toList())
        return Jwts.builder()
            .setSubject(userPrinciple.username)
            .claim("authorities", authorities) // Thêm quyền vào mã thông báo truy cập
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpiration * 1000L))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }

    fun createRefreshToken(authentication: Authentication): String {
        val userPrinciple: UserPrinciple = authentication.principal as UserPrinciple
        return Jwts.builder()
            .setSubject(userPrinciple.username)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtRefreshExpiration * 1000L))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }

    fun validateToken(token: String?): Boolean {
        try {
            Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
            return true
        } catch (e: SignatureException) {
            logger.error("Invalid JWT signature -> Message: ", e)
        } catch (e: MalformedJwtException) {
            logger.error("Invalid format Token -> Message: ", e)
        } catch (e: ExpiredJwtException) {
            logger.error("Expired JWT Token -> Message: ", e)
        } catch (e: UnsupportedJwtException) {
            logger.error("Unsupported JWT Token -> Message: ", e)
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string is empty -> Message: ", e)
        }

        return false
    }

    fun getUserNameFromToken(token: String?): String {
        return Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .body
            .subject
    }
}