package com.phoenix.amazon.AmazonBackend.external.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@FeignClient(name="email-verifier",url = "https://quickemailverification.p.rapidapi.com/v1")
public interface IEmailClient {
    String API_HOST_HEADER_NAME = "X-RapidAPI-Host";
    String API_KEY_HEADER_NAME = "X-RapidAPI-Key";
    String API_AUTHORIZATION_NAME="Authorization";

    @GetMapping("/verify")
    Map<String, String> verify(@RequestHeader(name = API_HOST_HEADER_NAME) String apiHostHeader,
                               @RequestHeader(name = API_KEY_HEADER_NAME) String apiKeyHeader,
                               @RequestHeader(name = API_AUTHORIZATION_NAME) String apiAuthHeader,
                               @RequestParam("params") Map<String, String> params);
}
