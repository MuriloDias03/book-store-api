package com.murilodias03.bookstore.data.dto.security;

import java.util.Date;

public record TokenDTO(
        String username,
        Boolean authenticated,
        Date created,
        Date expiration,
        String accessToken,
        String refreshToken
) {}