package com.hoangtien2k3.userservice.service

import com.hoangtien2k3.userservice.model.dto.request.SignInForm
import com.hoangtien2k3.userservice.model.dto.request.SignUpForm
import com.hoangtien2k3.userservice.model.dto.response.JwtResponse
import com.hoangtien2k3.userservice.model.entity.User
import reactor.core.publisher.Mono

interface IUserService {
    fun registerUser(signUpFrom: SignUpForm): Mono<User>

    fun login(signInForm: SignInForm): Mono<JwtResponse>
}