package com.unisender.responses;

import java.util.List;

public class ResponseWithWarnings <T> {
    private List<Warning> warnings;
    private T response;

    public ResponseWithWarnings(T response) {
        this.response = response;
    }

    public List<Warning> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<Warning> warnings) {
        this.warnings = warnings;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "ResponseWithWarnings{" +
                "warnings=" + warnings +
                ", response=" + response +
                '}';
    }
}
