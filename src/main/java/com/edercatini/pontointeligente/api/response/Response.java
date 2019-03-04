package com.edercatini.pontointeligente.api.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Response<T extends Serializable> {
    private T data;
    private List<String> errors;

    public Response() {
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<String> getErrors() {
        if (this.errors == null) {
            return new ArrayList<>();
        }

        return this.errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}