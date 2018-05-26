package com.mattmartin.faithbible.audiosearchapi.http;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FBCApiResponse<T> {
    private T domainModel;
    private List<String> errors = new ArrayList();
    private HttpStatus statusCode;

    public FBCApiResponse(T domainModel, String singleError) {
        this.statusCode = HttpStatus.OK;
        this.domainModel = domainModel;
        this.errors = Arrays.asList(new String[]{singleError});
    }

    public FBCApiResponse(HttpStatus statusCode) {
        this.statusCode = HttpStatus.OK;
        this.statusCode = statusCode;
    }


    public FBCApiResponse(HttpStatus statusCode, List<String> errors) {
        this.statusCode = HttpStatus.OK;

        this.errors = errors;
        this.statusCode = statusCode;
    }

    public FBCApiResponse(T domainModel, HttpStatus statusCode) {
        this.statusCode = HttpStatus.OK;
        this.domainModel = domainModel;
        this.statusCode = statusCode;
    }


    public List<String> getErrors() {
        return this.errors;
    }

    public T getBody() {
        return this.domainModel;
    }


    public boolean hasErrors() {
        return this.errors.size() > 0;
    }

    public boolean hasBody() {
        return null != this.domainModel;
    }

    public HttpStatus getStatusCode() {
        return this.statusCode;
    }
}
