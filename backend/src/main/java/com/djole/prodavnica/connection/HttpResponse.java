package com.djole.prodavnica.connection;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class HttpResponse {

    public static Response getResponseWithData(String message, Map<?, ?> data, HttpStatus httpStatus) {
        return new Response(message, data, httpStatus);
    }

    public static Response getResponse(String message, HttpStatus httpStatus) {
        return new Response(message, httpStatus);
    }
}
