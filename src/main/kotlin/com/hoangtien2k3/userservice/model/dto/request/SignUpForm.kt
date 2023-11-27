package com.hoangtien2k3.userservice.model.dto.request

import lombok.Getter
import lombok.RequiredArgsConstructor
import lombok.Setter

@Setter
@Getter
@RequiredArgsConstructor
class SignUpForm(
    val name: String,
    val username: String,
    val email: String,
    val password: String,
    val roles: Set<String>
) {

}