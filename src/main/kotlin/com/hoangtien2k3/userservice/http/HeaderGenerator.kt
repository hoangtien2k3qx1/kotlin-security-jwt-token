package com.hoangtien2k3.userservice.http

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import java.net.URI
import java.net.URISyntaxException

@Service
class HeaderGenerator {
    fun getHeadersForSuccessGetMethod(): HttpHeaders {
        val httpHeaders = HttpHeaders()
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8")
        return httpHeaders
    }

    fun getHeadersForError(): HttpHeaders {
        val httpHeaders = HttpHeaders()
        httpHeaders.add("Content-Type", "application/problem+json; charset=UTF-8")
        return httpHeaders
    }

    fun getHeadersForSuccessPostMethod(request: HttpServletRequest, newResourceId: Long): HttpHeaders {
        val httpHeaders = HttpHeaders()
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8")
        try {
            httpHeaders.location = URI(request.getRequestURI() + "/" + newResourceId)
        } catch (e: URISyntaxException) {
//            e.printStackTrace();
        }
        return httpHeaders
    }
}