package com.hoangtien2k3.userservice.model.dto.request

import lombok.Getter
import lombok.RequiredArgsConstructor
import lombok.Setter

@Getter
@Setter
@RequiredArgsConstructor
class SignInForm(
     val username: String,
     val password: String
) {

}