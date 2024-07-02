package com.sofka.auditoria;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoInitializer implements CommandLineRunner {

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @Override
    public void run(String... args) throws Exception {
        mongoTemplate.getCollectionNames()
                .doOnNext(name -> System.out.println("Collection: " + name))
                .subscribe();
    }
}
