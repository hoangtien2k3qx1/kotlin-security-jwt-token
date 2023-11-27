package com.hoangtien2k3.userservice.model.dto.request

import lombok.Getter

@Getter
class TokenValidationResponse(var message: String?) {

    fun TokenValidationResponse() {}

    fun TokenValidationResponse(message: String?) {
        this.message = message
    }

    fun setMessage(message: String?) {
        this.message = message
    }
}