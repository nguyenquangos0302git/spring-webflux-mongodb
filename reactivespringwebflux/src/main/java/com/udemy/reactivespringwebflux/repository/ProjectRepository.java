package com.udemy.reactivespringwebflux.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.udemy.reactivespringwebflux.model.Project;

import reactor.core.publisher.Flux;

public interface ProjectRepository extends ReactiveMongoRepository<Project, String> {
    //{"name" : name}
    public Flux<Project> findByName(String name);

    //{"name" : {"$ne" : name}}
    public Flux<Project> findByNameNot(String name);

    //{"cost" : {"$gt" : cost}}
    public Flux<Project> findByEstimatedCostGreaterThan(Long cost);
    //{"cost" : {"$gt" : from, "$lt" : to}}

    public Flux<Project> findByEstimatedCostBetween(Long from, Long to);

    //{"name": /name/ }
    public Flux<Project> findByNameLike(String name);

    //{"name" : {"$regex" : name }} =>/^Pro/
    public Flux<Project> findByNameRegex(String name);

    @Query("{'name' : ?0}")
    public Flux<Project> findProjectByNameQuery(String name);

    @Query("{'name' : ?0 , 'cost' : ?1}")
    Flux<Project> findProjectByNameAndCostQuery(String name, Long cost);

    @Query("{cost : {$lt : ?1, $gt : ?0}}")
    Flux<Project> findByEstimatedCostBetweenQuery(Long from, Long to, org.springframework.data.domain.Sort sort);

    @Query(value = "{ 'name' : { $regex: ?0 } }", fields = "{'name' : 1,'cost':1}")
    Flux<Project> findByNameRegexQuery(String regexp);

}
