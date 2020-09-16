package com.example.demo.config;

import com.example.demo.model.RetryModel;
import com.example.demo.repository.RetriesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class TrialRetryListener extends RetryListenerSupport {

    public static final String RETRY_MODEL_ID = "RETRY_MODEL_ID";
    public static final String TRIAL_EVENT = "TRIAL";
    public static final String IN_PROGRESS = "IN_PROGRESS";
    public static final String FAILED = "FAILED";
    public static final String SUCCESS = "SUCCESS";
    private static Logger LOGGER = LoggerFactory.getLogger(TrialRetryListener.class);

    private final RetriesRepository repository;

    public TrialRetryListener(RetriesRepository repository) {
        this.repository = repository;
    }

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
        LOGGER.info("ApiRetryListener.open");
        return super.open(context, callback);
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        LOGGER.info("ApiRetryListener.onError");
        RetryModel retryModel = new RetryModel();
        if (context.getAttribute(RETRY_MODEL_ID) != null) {
            long idSaved = (long) context.getAttribute(RETRY_MODEL_ID);
            retryModel = repository.findById(idSaved).orElse(new RetryModel());
        }
        retryModel.setRetry(retryModel.getRetry() + 1);
        retryModel.setEvent(TRIAL_EVENT);
        retryModel.setStatusResult(IN_PROGRESS);
        repository.save(retryModel);
        context.setAttribute(RETRY_MODEL_ID, retryModel.getId());

        LOGGER.info("Retry model {}", retryModel);
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        LOGGER.info("ApiRetryListener.close");
        LOGGER.info("Context {}", context);
        LOGGER.info("isExhausted {}", isExhausted(context));
        LOGGER.info("isClosed {}", isClosed(context));
        LOGGER.info("isRecovered {}", isRecovered(context));

        if (context.getAttribute(RETRY_MODEL_ID) == null) {
            LOGGER.error("Error to locate context for {}", RETRY_MODEL_ID);
            return;
        }
        long idSaved = (long) context.getAttribute(RETRY_MODEL_ID);
        repository.findById(idSaved).ifPresent(r -> {
            if (isExhausted(context)) {
                r.setStatusResult(FAILED);
            } else {
                r.setStatusResult(SUCCESS);
            }
            repository.save(r);
            LOGGER.info("Retry model {}", r);
        });

    }

    private boolean isExhausted(RetryContext context) {
        return context.hasAttribute(RetryContext.EXHAUSTED);
    }

    private boolean isClosed(RetryContext context) {
        return context.hasAttribute(RetryContext.CLOSED);
    }

    private boolean isRecovered(RetryContext context) {
        return context.hasAttribute(RetryContext.RECOVERED);
    }

}
