package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ServiceImpl implements Service
{
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Override
    public Mono<Response> handle(Request request)
    {
        return Mono.just("OK")
                   .map(response -> {
                       logger.info("Response: {}", response);

                       return new Response.Builder().status("OK")
                                                    .build();
                   });
    }
}
