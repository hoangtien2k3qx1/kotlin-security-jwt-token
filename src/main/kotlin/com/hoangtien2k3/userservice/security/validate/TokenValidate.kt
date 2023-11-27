package com.hoangtien2k3.userservice.security.validate

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureException
import org.springframework.stereotype.Component

@Component
class TokenValidate {

    // @Value("${jwt.secret}")
    private val SECRET_KEY: String? = "vip2023"

    fun validateToken(token: String): Boolean {
        var token = token
        require(!(SECRET_KEY == null || SECRET_KEY.isEmpty())) { "Not found secret key in structure" }
        if (token.startsWith("Bearer ")) token = token.replace("Bearer ", "")
        return try {
            val claims = Jwts
                .parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .body

            // Kiểm tra thời gian hết hạn của token
            val currentTimeMillis = System.currentTimeMillis()

            // Token đã hết hạn
            claims.expiration.time >= currentTimeMillis
        } catch (ex: ExpiredJwtException) {
            throw IllegalArgumentException("Token has expired.")
        } catch (ex: MalformedJwtException) {
            throw IllegalArgumentException("Invalid token.")
        } catch (ex: SignatureException) {
            throw IllegalArgumentException("Token validation error.")
        } catch (ex: IllegalArgumentException) {
            throw IllegalArgumentException("Token validation error: " + ex.message)
        }
    }

}