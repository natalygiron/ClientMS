package com.bootcamp.clientms.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class RequestPathFilter implements WebFilter {

  public static final String PATH_ATTRIBUTE = "requestPath";

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String path = exchange.getRequest().getPath().value();
    exchange.getAttributes().put(PATH_ATTRIBUTE, path);
    return chain.filter(exchange);
  }
}

