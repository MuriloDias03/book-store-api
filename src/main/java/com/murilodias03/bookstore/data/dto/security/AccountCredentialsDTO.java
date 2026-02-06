package com.murilodias03.bookstore.data.dto.security;

public record AccountCredentialsDTO(
        String username,
        String password
) {}