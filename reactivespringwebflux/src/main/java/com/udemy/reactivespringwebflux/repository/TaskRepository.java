package com.udemy.reactivespringwebflux.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.udemy.reactivespringwebflux.model.Task;

public interface TaskRepository extends ReactiveMongoRepository<Task, String> {

}
