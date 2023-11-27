package com.hoangtien2k3.userservice.controller

import com.hoangtien2k3.userservice.model.dto.request.SignInForm
import com.hoangtien2k3.userservice.model.dto.request.SignUpForm
import com.hoangtien2k3.userservice.model.dto.request.TokenValidationResponse
import com.hoangtien2k3.userservice.model.dto.response.JwtResponse
import com.hoangtien2k3.userservice.model.dto.response.ResponseMessage
import com.hoangtien2k3.userservice.model.entity.User
import com.hoangtien2k3.userservice.security.jwt.JwtProvider
import com.hoangtien2k3.userservice.security.validate.AuthorityTokenUtil
import com.hoangtien2k3.userservice.security.validate.TokenValidate
import com.hoangtien2k3.userservice.service.impl.UserServiceImpl
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/auth")
class UserController {
    @Autowired
    private val userService: UserServiceImpl? = null

    @Autowired
    private val jwtProvider: JwtProvider? = null

    @PostMapping("/signup")
    fun register(@Valid @RequestBody signUpForm: SignUpForm): Mono<ResponseEntity<ResponseMessage>> {
        return userService!!.registerUser(signUpForm)
            .flatMap {
                Mono.just(
                    ResponseEntity(
                        ResponseMessage("User: " + signUpForm.username + " create successfully."),
                        HttpStatus.OK
                    )
                )
            }
            .onErrorResume { error: Throwable ->
                Mono.just(
                    ResponseEntity(
                        ResponseMessage(error.message),
                        HttpStatus.BAD_REQUEST
                    )
                )
            }
    }

    @PostMapping("/signin")
    fun login(@Valid @RequestBody signInForm: SignInForm?): Mono<ResponseEntity<JwtResponse>> {
        return userService!!.login(signInForm!!)
            .map { body: JwtResponse? ->
                ResponseEntity.ok(
                    body
                )
            }
            .onErrorResume { error: Throwable? ->
                val errorResponse = JwtResponse(null, null, null, null, null)
                Mono.just(
                    ResponseEntity(
                        errorResponse,
                        HttpStatus.UNAUTHORIZED
                    )
                )
            }
    }

    @PostMapping("/refresh")
    fun refresh(@RequestHeader("Refresh-Token") refreshToken: String?): Mono<ResponseEntity<JwtResponse?>> {
        return userService!!.refreshToken(refreshToken)
            .map { newAccessToken: String? ->
                val jwtResponse = JwtResponse(newAccessToken, null, null, null, null)
                ResponseEntity.ok(jwtResponse)
            }
            .onErrorResume { error: Throwable? ->
                Mono.just(
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
                )
            }
    }

    @GetMapping("/validateToken")
    fun validateToken(@RequestHeader(name = "Authorization") authorizationToken: String?): Boolean {
        val validate = TokenValidate()
        return if (validate.validateToken(authorizationToken!!)) {
            ResponseEntity.ok(TokenValidationResponse("Valid token")).hasBody()
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(TokenValidationResponse("Invalid token")).hasBody()
        }
    }

    // check role token authorities
    @GetMapping("/hasAuthority")
    fun getAuthority(
        @RequestHeader(name = "Authorization") authorizationToken: String?,
        requiredRole: String
    ): Boolean {
        val authorityTokenUtil = AuthorityTokenUtil()
        val authorities: List<Any?>? = authorizationToken?.let { authorityTokenUtil.checkPermission(it) }
        return if (authorities != null) {
            if (authorities.contains(requiredRole)) {
                ResponseEntity.ok(TokenValidationResponse("Role access api")).hasBody()
            } else {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(TokenValidationResponse("Invalid token")).hasBody()
            }
        } else false
    }
}