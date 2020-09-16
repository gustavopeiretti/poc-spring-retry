package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class TrialExtensionRequest implements Serializable {

    @JsonProperty("card_number")
    @NotBlank(message = "credential can not be null or empty")
    private String credential;

    @JsonProperty("extended_days")
    @NotNull
    @Min(1)
    private int extendedDays;

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public int getExtendedDays() {
        return extendedDays;
    }

    public void setExtendedDays(int extendedDays) {
        this.extendedDays = extendedDays;
    }

    @Override
    public String toString() {
        return "TrialExtensionRequest{" +
                "credential='" + credential + '\'' +
                ", extendedDays=" + extendedDays +
                '}';
    }
}
