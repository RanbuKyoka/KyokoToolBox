package com.rankukyoko.toolbox.kyokotoolbox.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@RestController
public class KyokoToolBoxController {
    @Bean
    public RouterFunction<ServerResponse> indexRouter(
            @Value("classpath:/templates/index.html") final Resource indexHtml) {
        return route(GET("/index"), request -> ok().contentType(MediaType.TEXT_HTML).bodyValue(indexHtml));
    }

    @RequestMapping("/indexsss")
    public String sayHello() {
        return "indexsss";
    }
}
