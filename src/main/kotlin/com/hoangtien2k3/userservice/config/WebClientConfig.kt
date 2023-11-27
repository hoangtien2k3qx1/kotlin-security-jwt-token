package com.hoangtien2k3.userservice.config

import org.springframework.context.annotation.Bean
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import java.net.http.HttpClient

class WebClientConfig {
//    @Bean
//    fun webClientBuilder(): WebClient.Builder {
//        return WebClient.builder()
//            .clientConnector(
//                ReactorClientHttpConnector(
//                    HttpClient.newConnection()
//                        .compress(true) // (Optional) Enable compression
//                        .port(8080) // Port of the service you want to connect to
//                        .keepAlive(true)
//                )
//            )
//    }
}