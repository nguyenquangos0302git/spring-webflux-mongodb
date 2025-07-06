package com.udemy.reactivespringwebflux.service;

import com.udemy.reactivespringwebflux.model.Project;
import com.udemy.reactivespringwebflux.model.Task;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProjectService {
    public Mono<Project> createProject(Project project);

    public Mono<Task> createTask(Task task);

    public Flux<Project> findAll();

    public Mono<Project> findById(String id);

    public Mono<Void> deleteById(String id);

    public Flux<Project> findByName(String name);

    public Flux<Project> findByNameNot(String name);

    public Flux<Project> findByEstimatedCostGreaterThan(Long cost);

    public Flux<Project> findByEstimatedCostBetween(Long from, Long to);

    public Flux<Project> findByNameLike(String name);

    public Flux<Project> findByNameRegex(String name);

    Flux<Project> findProjectByNameQuery(String name);

    Flux<Project> findProjectByNameAndCostQuery(String name, Long cost);

    Flux<Project> findByEstimatedCostBetweenQuery(Long from, Long to);

    Flux<Project> findByNameRegexQuery(String regexp);

    public Flux<Project> findProjectByNameQueryWithTemplate(String name);

    public Flux<Project> findByEstimatedCostBetweenQueryWithTemplate(Long from, Long to);

    public Flux<Project> findByNameRegexQueryWithTemplate(String regexp);

    public Mono<Void> upsertCostWithCriteriaTemplate(String id, Long cost);

    public Mono<Void> deleteWithCriteriaTemplate(String id);

    public Mono<Long> findNoOfProjectsCostGreaterThan(Long cost);

    public Flux<ResultByStartDateAndCost> findCostsGroupByStartDateForProjectsCostGreaterThan(Long cost);

    public Flux<ResultProjectTasks> findAllProjectTasks();

    public Mono<Void> saveProjectAndTask(Mono<Project> p, Mono<Task> t);

    public Mono<Void> chunkAndSaveProject(Project p);

    public Mono<Project> loadProjectFromGrid(String projectId);

    public Mono<Void> deleteProjectFromGrid(String projectId);

    Flux<Project> findNameDescriptionForMatchingTerm(String term);

    Flux<Project> findNameDescriptionForMatchingAny(String... words);

}
