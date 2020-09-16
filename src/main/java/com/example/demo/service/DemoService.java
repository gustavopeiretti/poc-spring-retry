package com.example.demo.service;

import com.example.demo.config.WebClientConfig;
import com.example.demo.exception.RetryTrialException;
import com.example.demo.model.DemoRequest;
import com.example.demo.model.DemoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DemoService {

    private static Logger LOGGER = LoggerFactory.getLogger(DemoService.class);

    private final WebClientConfig webClient;
    private final RetryTemplate retryTemplate;

    public DemoService(WebClientConfig webClient, @Qualifier("retry-trial") RetryTemplate retryTemplate) {
        this.webClient = webClient;
        this.retryTemplate = retryTemplate;
    }

//    @Retryable(value = {RetryTrialException.class}, maxAttempts = 2, backoff = @Backoff(delay = 3000))
//    public DemoResponse search(String name) {
//        LOGGER.info("Trying " + LocalDate.now().toString());
//        DemoRequest request = new DemoRequest();
//        request.setName(name);
//        DemoResponse search = webClient.search(request);
//        return search;
//    }
//
//    @Recover
//    public DemoResponse searchRecovery(RuntimeException t, String s) {
//        LOGGER.info(String.format("Retry Recovery - %s", t.getMessage()));
//        return null;
//    }


    // parte uno sin retry
    public DemoResponse searchTest(String name) {
        LOGGER.info("Trying {} ", LocalDate.now());
        DemoRequest request = new DemoRequest();
        request.setName(name);
        DemoResponse search;
        try {
            search = webClient.search(request);
        } catch (RetryTrialException e) {
            // guardar en tabla el error
            throw new RuntimeException("Error"); // TODO..
        }
        return search;
    }

    // parte dos con retry
    public DemoResponse search(String name) {

        DemoResponse response = retryTemplate.execute(
                retryContext -> {
                    LOGGER.info("Trying {} ", LocalDate.now());
                    DemoRequest request = new DemoRequest();
                    request.setName(name);
                    if (name.equals("gus")) {
                        // simulate OK after 3 retries
                        if (retryContext.getRetryCount() <= 3) {
                            return webClient.search(request);
                        } else {
                            return webClient.searchOK(request);
                        }
                    } else {
                        return webClient.search(request);
                    }
                },
                context -> null
        );
        LOGGER.info(String.format("Returning {}", response));
        return response;
    }
}
