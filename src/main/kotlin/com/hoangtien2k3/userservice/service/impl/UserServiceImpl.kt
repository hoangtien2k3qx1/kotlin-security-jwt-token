package com.hoangtien2k3.userservice.service.impl


import com.hoangtien2k3.userservice.model.dto.model.TokenManager
import com.hoangtien2k3.userservice.model.dto.request.SignInForm
import com.hoangtien2k3.userservice.model.dto.request.SignUpForm
import com.hoangtien2k3.userservice.model.dto.response.JwtResponse
import com.hoangtien2k3.userservice.model.entity.Role
import com.hoangtien2k3.userservice.model.entity.RoleName
import com.hoangtien2k3.userservice.model.entity.User
import com.hoangtien2k3.userservice.repository.IRoleRepository
import com.hoangtien2k3.userservice.repository.IUserRepository
import com.hoangtien2k3.userservice.security.jwt.JwtProvider
import com.hoangtien2k3.userservice.security.userprinciple.UserDetailService
import com.hoangtien2k3.userservice.security.userprinciple.UserPrinciple
import com.hoangtien2k3.userservice.service.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatusCode
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.util.*
import java.util.function.Consumer


@Service
class UserServiceImpl @Autowired constructor(
    var userRepository: IUserRepository? = null,
    var roleRepository: IRoleRepository? = null,
    var passwordEncoder: PasswordEncoder? = null,
    var jwtProvider: JwtProvider? = null,
    var tokenManager: TokenManager? = null,
    var userDetailsService: UserDetailService? = null

) : IUserService {

    @Autowired
    private val webClientBuilder: WebClient.Builder? = null

    @Value("\${refresh.token.url}") // Đường dẫn endpoint để refresh token

    private val refreshTokenUrl: String? = null
    private final var imageUrl: String = "https://www.facebook.com/photo/?fbid=723931439407032&set=pob.100053705482952"

    // register user
    override fun registerUser(signUpForm: SignUpForm): Mono<User> {
        return Mono.defer<User> {
            if (existsByUsername(signUpForm.username)) {
                return@defer Mono.error<User>(
                    RuntimeException(
                        "The username " + signUpForm.username + " is existed, please try again."
                    )
                )
            }
            if (existsByEmail(signUpForm.email)) {
                return@defer Mono.error<User>(
                    RuntimeException(
                        "The email " + signUpForm.email + " is existed, please try again."
                    )
                )
            }
            val roles: MutableSet<Role> = HashSet()
            signUpForm.roles.forEach(Consumer { role: String? ->
                val roleName: RoleName? = when (role) {
                    "admin", "ADMIN", "ROLE_ADMIN", "role_admin" -> RoleName.ROLE_ADMIN
                    "PM", "pm", "ROLE_PM", "role_pm" -> RoleName.ROLE_PM
                    "USER", "user", "ROLE_USER", "role_user" -> RoleName.ROLE_USER
                    else -> null
                }
                val userRole = roleName?.let {
                    roleRepository!!.findByName(it)
                        .orElseThrow {
                            RuntimeException(
                                "Role not found database."
                            )
                        }
                }
                if (userRole != null) {
                    roles.add(userRole)
                }
            })


            /**
             * @author: hoangtien2k3
             * @create: 27/11/2023 - 16:36
             * @file: UserServiceImpl.kt
             * @update: 27/11/2023
             * @description: @Builder
             */
//            val user: User = User.builder
//                .name(signUpForm.name)
//                .username(signUpForm.username)
//                .email(signUpForm.email)
//                .password(passwordEncoder!!.encode(signUpForm.password))
//                .avatar(imageUrl)
//                .roles(roles)
//                .build()

            // using @Builder primary constructor
            val user: User = User(
                null,
                signUpForm.name,
                signUpForm.username,
                signUpForm.email,
                passwordEncoder!!.encode(signUpForm.password),
                imageUrl,
                roles
            )

            userRepository!!.save(user)
            Mono.just<User>(user)
        }
    }

    // login email or username
    override fun login(signInForm: SignInForm): Mono<JwtResponse> {
        return Mono.defer {
            val usernameOrEmail = signInForm.username
            val isEmail = usernameOrEmail.contains("@")
            val userDetails: UserDetails = if (isEmail) {
                userDetailsService!!.loadUserByEmail(usernameOrEmail)
            } else {
                userDetailsService!!.loadUserByUsername(usernameOrEmail)
            }
            val authentication: Authentication =
                UsernamePasswordAuthenticationToken(userDetails, signInForm.password, userDetails.authorities)
            SecurityContextHolder
                .getContext().authentication = authentication

            // Generate token and refresh token using JwtProvider
            val accessToken = jwtProvider!!.createToken(authentication)
            val refreshToken = jwtProvider!!.createRefreshToken(authentication)
            val userPrinciple = userDetails as UserPrinciple

            // Store the token and refresh token using TokenManager
            tokenManager!!.storeToken(userPrinciple.getUsername()!!, accessToken)
            tokenManager!!.storeRefreshToken(userPrinciple.getUsername()!!, refreshToken)
            val jwtResponse = JwtResponse(
                accessToken,
                refreshToken,
                userPrinciple.id,
                userPrinciple.name,
                userPrinciple.authorities
            )
            Mono.just(jwtResponse)
        }
    }

    fun refreshToken(refreshToken: String?): Mono<String> {
        return webClientBuilder!!.build()
            .post()
            .uri(refreshTokenUrl!!)
            .header("Refresh-Token", refreshToken)
            .retrieve()
            .onStatus(
                { obj: HttpStatusCode -> obj.is4xxClientError }
            ) {
                Mono.error<Throwable?>(
                    IllegalArgumentException("Refresh token không hợp lệ")
                )
            }
            .bodyToMono<JwtResponse>(JwtResponse::class.java)
            .map<String>(JwtResponse::accessToken)
    }

    fun findById(id: Long?): Optional<User> {
        return Optional.ofNullable(
            userRepository!!.findById(id!!)
        ).orElseThrow {
            UsernameNotFoundException("User not found.")
        }
    }

    // get all user in list user
    fun getAllUsers(): Optional<List<User>> {
        return Optional.ofNullable(
            userRepository!!.findAll()
        )
    }

    // load user by page and size
    fun getAllUsers(page: Int, size: Int): Page<User> {
        val pageable: Pageable = PageRequest.of(page, size)
        return userRepository!!.findAll(pageable)
    }

    fun existsByUsername(username: String?): Boolean {
        return userRepository!!.existsByUsername(username!!)
    }

    fun existsByEmail(email: String?): Boolean {
        return userRepository!!.existsByEmail(email!!)
    }

}