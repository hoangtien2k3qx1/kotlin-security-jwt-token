package com.hoangtien2k3.userservice.model.dto.response

import jakarta.validation.constraints.Size
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.Setter

@Getter
@Setter
@AllArgsConstructor
class ResponseMessage {
    @Size(min = 10, max = 500)
    private val message: String? = null
}