package com.hoangtien2k3.userservice.security.validate

import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value

class AuthorityTokenUtil {
    @Value("\${jwt.secret}")
    private val jwtSecret: String? = null

    fun checkPermission(token: String): List<Any?>? {
        try {
            val claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .body

            // claims["authorities"] as? MutableList<String>
            return claims.get("authorities", MutableList::class.java)
        } catch (e: Exception) {
            return null
        }
    }

}

