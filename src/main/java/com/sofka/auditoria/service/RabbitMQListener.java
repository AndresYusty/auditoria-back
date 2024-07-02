package com.sofka.auditoria.service;

import com.sofka.auditoria.model.Auditoria;
import com.sofka.auditoria.repository.AuditoriaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Objects;

@Service
public class RabbitMQListener {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQListener.class);

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @RabbitListener(queues = "auditoria-queue")
    public void receiveMessage(Map<String, Object> message) {
        logger.info("Received message: {}", message);

        String usuario = (String) message.get("usuario");
        String accion = (String) message.get("accion");
        String fechaStr = (String) message.get("fecha");

        if (Objects.isNull(usuario) || Objects.isNull(accion) || Objects.isNull(fechaStr)) {
            logger.error("Invalid message format: {}", message);
            return;
        }

        try {
            LocalDateTime fecha = LocalDateTime.parse(fechaStr);
            Auditoria auditoria = new Auditoria();
            auditoria.setUsuario(usuario);
            auditoria.setAccion(accion);
            auditoria.setFecha(fecha);

            auditoriaRepository.save(auditoria)
                    .doOnNext(aud -> logger.info("Saved auditoria: {}", aud))
                    .doOnError(e -> logger.error("Error saving auditoria", e))
                    .subscribe();

        } catch (DateTimeParseException e) {
            logger.error("Error parsing date: {}", fechaStr, e);
        }
    }
}
