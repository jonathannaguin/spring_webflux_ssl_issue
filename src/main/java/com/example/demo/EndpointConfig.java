package com.example.demo;

import io.netty.channel.ChannelOption;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.actuate.info.MapInfoContributor;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class EndpointConfig
{

    @Bean
    public InfoEndpoint infoEndpoint()
    {
        Map<String, Object> info = new HashMap<>();

        info.put("version",
                 EndpointConfig.class.getPackage()
                                     .getImplementationVersion());

        InfoContributor manifest = new MapInfoContributor(info);

        return new InfoEndpoint(Collections.singletonList(manifest));
    }

    @Bean
    public NettyServerCustomizer nettyServerCustomizer()
    {
        return httpServer -> httpServer.idleTimeout(Duration.ofMinutes(10))
                                       .option(ChannelOption.SO_BACKLOG, 65535)
                                       .option(ChannelOption.SO_REUSEADDR, true)
                                       .childOption(ChannelOption.TCP_NODELAY, true)
                                       .childOption(ChannelOption.SO_KEEPALIVE, true);
    }
}
