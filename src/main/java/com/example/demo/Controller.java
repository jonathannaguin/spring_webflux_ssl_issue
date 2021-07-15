package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/demo/")
@Validated
public class Controller
{
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    private Service service;

    @PostMapping(value = { "/request" }, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Response> request(@Valid @RequestBody Request request)
    {
        // new RequestValidatorForSMS().validate(request);

        logger.info("Request => {}", request);

        return service.handle(request);
    }

    @Autowired
    public void setService(Service service)
    {
        this.service = service;
    }
}
