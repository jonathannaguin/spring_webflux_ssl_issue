package com.example.demo;

import reactor.core.publisher.Mono;

public interface Service
{
    Mono<Response> handle(Request request);
}
