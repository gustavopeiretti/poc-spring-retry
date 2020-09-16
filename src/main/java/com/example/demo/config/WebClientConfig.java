package com.example.demo.config;

import com.example.demo.exception.RetryTrialException;
import com.example.demo.model.DemoRequest;
import com.example.demo.model.DemoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class WebClientConfig {

    private final WebClient webClient;

    public WebClientConfig(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://run.mocky.io").build();
    }

    public DemoResponse search(DemoRequest request) {
        return this.webClient.post()
                .uri("/v3/ba2fa3df-9251-46b2-b8c0-d5b61af1a36d")
                .body(Mono.just(request), DemoRequest.class)
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new RetryTrialException()))
                .bodyToMono(DemoResponse.class)
                .block();
    }


    public DemoResponse searchOK(DemoRequest request) {
        return this.webClient.post()
                .uri("/v3/dc319eb3-26c2-48c9-b7bd-4a1fd4f726eb")
                .body(Mono.just(request), DemoRequest.class)
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new RetryTrialException()))
                .bodyToMono(DemoResponse.class)
                .block();
    }


}
