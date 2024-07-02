package com.sofka.auditoria.service;

import com.sofka.auditoria.model.Auditoria;
import com.sofka.auditoria.repository.AuditoriaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class RabbitMQListener {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQListener.class);

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @RabbitListener(queues = "auditoria-queue")
    public void receiveMessage(Map<String, Object> message) {
        logger.info("Received message: {}", message);
        Auditoria auditoria = new Auditoria();
        auditoria.setUsuario((String) message.get("usuario"));
        auditoria.setAccion((String) message.get("accion"));
        auditoria.setFecha(LocalDateTime.parse((String) message.get("fecha")));

        auditoriaRepository.save(auditoria)
                .doOnNext(aud -> logger.info("Saved auditoria: {}", aud))
                .doOnError(e -> logger.error("Error saving auditoria", e))
                .subscribe();
    }
}
