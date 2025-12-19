package com.schedula.schedula.core;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class DynamicResponseEntity extends ResponseEntity<Object> {
    public DynamicResponseEntity(HttpStatusCode httpStatusCode, HttpHeaders headers, Object body) {
        super(body, headers, httpStatusCode);
    }

    public DynamicResponseEntity withStatusCode(HttpStatus status) {
        return new DynamicResponseEntity(status, this.getHeaders(), this.getBody());
    }

    public DynamicResponseEntity withHeader(String headerName, String headerValue) {
        HttpHeaders headers = new HttpHeaders(this.getHeaders());
        headers.add(headerName, headerValue);
        return new DynamicResponseEntity(this.getStatusCode(), headers, this.getBody());
    }

    public DynamicResponseEntity withBody(Object body) {
        return new DynamicResponseEntity(this.getStatusCode(), this.getHeaders(), body);
    }
}
