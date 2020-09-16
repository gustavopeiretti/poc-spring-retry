package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.StringJoiner;

@Entity
public class RetryModel {

    @Id
    @GeneratedValue
    private Long id;
    private String event;
    private String body;
    private int retry;
    private String statusResult;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public String getStatusResult() {
        return statusResult;
    }

    public void setStatusResult(String statusResult) {
        this.statusResult = statusResult;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RetryModel.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("event='" + event + "'")
                .add("body='" + body + "'")
                .add("retry=" + retry)
                .add("statusResult='" + statusResult + "'")
                .toString();
    }
}
