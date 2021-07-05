package com.example.demo;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

@Configuration
@Profile("dev")
public class SwaggerConfig implements WebFilter
{
    @Bean
    public OpenAPI customOpenAPI(@Value("${VERSION:SNAPSHOT}") String appVersion)
    {
        return new OpenAPI().components(new Components().addSecuritySchemes("BasicAuth",
                                                                            new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                                                                                .scheme("basic")))
                            .info(new Info().title("My API")
                                            .version(appVersion));
    }

    @Override
    public @Nonnull
    Mono<Void> filter(@Nonnull ServerWebExchange exchange,
                      @Nonnull WebFilterChain chain)
    {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath()
                             .pathWithinApplication()
                             .value();

        if (path.startsWith("/demo/webjars"))
        {
            exchange = exchange.mutate()
                               .request(request.mutate()
                                               .path(path.substring("/demo".length()))
                                               .build())
                               .build();
        }
        return chain.filter(exchange);
    }
}
