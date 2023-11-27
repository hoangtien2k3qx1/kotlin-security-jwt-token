package com.hoangtien2k3.userservice.model.dto.request

import lombok.Getter

@Getter
class TokenValidationRequest(var accessToken: String?) {

    fun TokenValidationRequest() {}

    fun TokenValidationRequest(accessToken: String?) {
        this.accessToken = accessToken
    }

    fun setAccessToken(accessToken: String?) {
        this.accessToken = accessToken
    }
}