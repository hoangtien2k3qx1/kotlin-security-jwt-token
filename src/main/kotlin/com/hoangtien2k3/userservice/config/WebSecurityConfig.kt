package com.hoangtien2k3.userservice.config

import com.hoangtien2k3.userservice.security.jwt.JwtEntryPoint
import com.hoangtien2k3.userservice.security.jwt.JwtTokenFilter
import com.hoangtien2k3.userservice.security.userprinciple.UserDetailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity
class WebSecurityConfig : WebSecurityConfigAdaptor {

    @Autowired
    private val userDetailService: UserDetailService? = null

    @Autowired
    private val jwtEntryPoint: JwtEntryPoint? = null

    @Bean
    fun jwtTokenFilter(): JwtTokenFilter {
        return JwtTokenFilter()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }

    @Throws(Exception::class)
    fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder) {
        authenticationManagerBuilder
            .userDetailsService(userDetailService)
            .passwordEncoder(passwordEncoder())
    }

    @Throws(Exception::class)
    protected fun configure(httpSecurity: HttpSecurity) {
        httpSecurity
            .cors() // Cross-Origin Resource Sharing (CORS):
            .and()
            .csrf().disable() // CSRF (Cross-Site Request Forgery)
            .authorizeRequests()
            .antMatchers("/api/auth/**").permitAll()
            .antMatchers("/api/manager/token").permitAll()
            .anyRequest().authenticated()
            .and()
            .exceptionHandling().authenticationEntryPoint(jwtEntryPoint)
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        httpSecurity.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }

}