package com.hoangtien2k3.userservice.model.dto.model

import lombok.Getter
import lombok.Setter
import org.springframework.stereotype.Component

@Setter
@Getter
@Component
class TokenManager {
    var TOKEN: String? = null
    var REFRESHTOKEN: String? = null
    private val tokenStore: MutableMap<String, String> = HashMap()
    private val refreshTokenStore: MutableMap<String, String> = HashMap()

    // Lưu trữ token cho một tên người dùng
    fun storeToken(username: String, token: String) {
        tokenStore[username] = token
        TOKEN = token
    }

    fun storeRefreshToken(username: String, refreshToken: String) {
        refreshTokenStore[username] = refreshToken
        REFRESHTOKEN = refreshToken
    }

    // Lấy token dựa trên tên người dùng
    fun getTokenByUsername(username: String): String? {
        return tokenStore[username]
    }

    // Xóa token dựa trên tên người dùng (ví dụ: khi đăng xuất)
    fun removeToken(username: String) {
        tokenStore.remove(username)
    }

}