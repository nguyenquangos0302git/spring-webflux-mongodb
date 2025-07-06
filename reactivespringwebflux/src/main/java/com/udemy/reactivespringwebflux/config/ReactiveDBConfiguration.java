package com.udemy.reactivespringwebflux.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.udemy.reactivespringwebflux.repository")
public class ReactiveDBConfiguration extends AbstractReactiveMongoConfiguration {

    @Value("${udemy.mongodb.replicaset.name}")
    private String replicasetName;

    @Value("${udemy.mongodb.replicaset.username}")
    private String replicasetUsername;
    @Value("${udemy.mongodb.replicaset.password}")
    private String replicasetPassword;

    @Value("${udemy.mongodb.replicaset.primary}")
    private String replicasetPrimary;

    @Value("${udemy.mongodb.replicaset.port}")
    private String replicasetPort;

    @Value("${udemy.mongodb.replicaset.database}")
    private String database;

    @Value("${udemy.mongodb.replicaset.authentication-database}")
    private String replicasetAuthenticationDb;
//
//    @Autowired
//    private MappingMongoConverter mongoConverter;

    @Override
    public MongoClient reactiveMongoClient() {

        return MongoClients.create("mongodb://" + replicasetUsername + ":" + replicasetPassword + "@"
                + replicasetPrimary + ":" + replicasetPort + "/" + database + "?replicaSet=" + replicasetName
                + "&authSource=" + replicasetAuthenticationDb);

    }

    @Override
    protected String getDatabaseName() {
        // TODO Auto-generated method stub
        return database;
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
    }

    @Bean
    ReactiveMongoTransactionManager transactionManager(ReactiveMongoDatabaseFactory factory) {
        return new ReactiveMongoTransactionManager(factory);
    }

    @Bean
    public ReactiveGridFsTemplate reactiveGridFsTemplate(MappingMongoConverter mongoConverter) throws Exception {
        return new ReactiveGridFsTemplate(reactiveMongoDbFactory(), mongoConverter);
    }
}
