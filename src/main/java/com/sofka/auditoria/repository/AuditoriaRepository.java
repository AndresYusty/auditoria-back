package com.sofka.auditoria.repository;

import com.sofka.auditoria.model.Auditoria;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AuditoriaRepository extends ReactiveMongoRepository<Auditoria, String> {
}
