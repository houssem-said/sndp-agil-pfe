package com.sndp.agil.backend.security;

import io.jsonwebtoken.Claims;

@FunctionalInterface
interface ClaimsResolver<T> {
    T resolve(Claims claims);
}
