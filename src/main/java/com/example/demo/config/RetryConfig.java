package com.example.demo.config;

import com.example.demo.exception.RetryTrialException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@EnableRetry
public class RetryConfig {

    @Bean("retry-trial")
    public RetryTemplate retryTemplate(TrialRetryListener trialRetryListener) {

        return RetryTemplate.builder()
                .maxAttempts(5)
                .exponentialBackoff(100, 2, 10000)
                .retryOn(RetryTrialException.class)
                .withListener(trialRetryListener)
                .build();

//        RetryTemplate retryTemplate = new RetryTemplate();
//
//        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
//        fixedBackOffPolicy.setBackOffPeriod(1000L);
//        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
//
//        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
//        retryPolicy.setMaxAttempts(4);
//        retryTemplate.setRetryPolicy(retryPolicy);
//
//        retryTemplate.registerListener(trialRetryListener);
//
//        return retryTemplate;
    }

}
