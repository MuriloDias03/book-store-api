package com.murilodias03.bookstore.config;

public interface TestConfigs {
    int SERVER_PORT = 8888;

    String HEADER_PARAM_AUTHORIZATION = "Authorization";
    String HEADER_PARAM_ORIGIN = "Origin";

    String ORIGIN_FRONTEND = "http://localhost:3000";
    String ORIGIN_LOCAL = "http://localhost:8080";
    String ORIGIN_ERROR = "http://localhost:3001";

}