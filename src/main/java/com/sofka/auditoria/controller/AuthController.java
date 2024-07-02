package com.sofka.auditoria.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/login")
    public Mono<Void> login() {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication().getName())
                .doOnNext(username -> {
                    Map<String, Object> message = new HashMap<>();
                    message.put("usuario", username);
                    message.put("accion", "LOGIN");
                    message.put("fecha", LocalDateTime.now().toString());
                    logger.info("Sending LOGIN event for user: {}", username);
                    rabbitTemplate.convertAndSend("auditoria-exchange", "auditoria", message);
                }).then();
    }

    @PostMapping("/logout")
    public Mono<Void> logout() {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication().getName())
                .doOnNext(username -> {
                    Map<String, Object> message = new HashMap<>();
                    message.put("usuario", username);
                    message.put("accion", "LOGOUT");
                    message.put("fecha", LocalDateTime.now().toString());
                    logger.info("Sending LOGOUT event for user: {}", username);
                    rabbitTemplate.convertAndSend("auditoria-exchange", "auditoria", message);
                }).then();
    }
}
