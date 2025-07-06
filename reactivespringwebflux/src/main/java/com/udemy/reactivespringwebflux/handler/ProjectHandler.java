package com.udemy.reactivespringwebflux.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.udemy.reactivespringwebflux.model.Project;
import com.udemy.reactivespringwebflux.model.Task;
import com.udemy.reactivespringwebflux.service.ProjectService;
import com.udemy.reactivespringwebflux.service.ResultByStartDateAndCost;
import com.udemy.reactivespringwebflux.service.ResultProjectTasks;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class ProjectHandler {

    @Autowired
    ProjectService projectService;

    public Mono<ServerResponse> createProject(ServerRequest serverRequest) {
        final Mono<Project> project = serverRequest.bodyToMono(Project.class);
        return project.flatMap(projectService::createProject)
                .flatMap(data -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(data));/*.onErrorResume(error -> { if (error instanceof
								  OptimisticLockingFailureException){ return
										  ServerResponse.status(HttpStatus.BAD_REQUEST).build(); 
								}
										 return
										  ServerResponse.status(500).build(); });*/

    }

    public Mono<ServerResponse> createTask(ServerRequest serverRequest) {
        final Mono<Task> task = serverRequest.bodyToMono(Task.class);
        return task.flatMap(projectService::createTask)
                .flatMap(data -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(data));
    }


    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(projectService.findAll(), Project.class);

    }

    public Mono<ServerResponse> findById(ServerRequest request) {

        String id = request.pathVariable("id");

        return projectService.findById(id)
                .flatMap(data -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(data))
                .switchIfEmpty(ServerResponse.notFound().build());

    }


    public Mono<ServerResponse> delete(ServerRequest request) {

        String id = request.pathVariable("id");

        return ok()

                .contentType(MediaType.APPLICATION_JSON)

                .body(projectService.deleteById(id), Void.class).log();

    }


    public Mono<ServerResponse> findByName(ServerRequest request) {
        String name = request.queryParam("name").get();

        return ok()

                .contentType(MediaType.APPLICATION_JSON)

                .body(projectService.findByName(name), Project.class).log();
    }

    public Mono<ServerResponse> findByNameNot(ServerRequest request) {
        String name = request.queryParam("name").get();

        return ok()

                .contentType(MediaType.APPLICATION_JSON)

                .body(projectService.findByNameNot(name), Project.class).log();
    }


    public Mono<ServerResponse> findByEstimatedCostGreaterThan(ServerRequest request) {
        String cost = request.queryParam("cost").get();

        return ok()

                .contentType(MediaType.APPLICATION_JSON)

                .body(projectService.findByEstimatedCostGreaterThan(Long.valueOf(cost)), Project.class).log();
    }


    public Mono<ServerResponse> findByEstimatedCostBetween(ServerRequest request) {
        String from = request.queryParam("from").get();
        String to = request.queryParam("to").get();
        return ok()

                .contentType(MediaType.APPLICATION_JSON)

                .body(projectService.findByEstimatedCostBetween(Long.valueOf(from), Long.valueOf(to)), Project.class).log();
    }


    public Mono<ServerResponse> findByNameLike(ServerRequest request) {
        String name = request.queryParam("name").get();

        return ok()

                .contentType(MediaType.APPLICATION_JSON)

                .body(projectService.findByNameLike(name), Project.class).log();
    }

    public Mono<ServerResponse> findByNameRegex(ServerRequest request) {
        String name = request.queryParam("name").get();
        String regex = "^" + name + "";
        return ok()

                .contentType(MediaType.APPLICATION_JSON)

                .body(projectService.findByNameRegex(regex), Project.class).log();
    }


    public Mono<ServerResponse> findByNameQuery(ServerRequest request) {
        String name = request.queryParam("name").get();

        return ok()

                .contentType(MediaType.APPLICATION_JSON)

                .body(projectService.findProjectByNameQuery(name), Project.class).log();
    }

    public Mono<ServerResponse> findByNameAndCostQuery(ServerRequest request) {
        String name = request.queryParam("name").get();
        String cost = request.queryParam("cost").get();
        return ok()

                .contentType(MediaType.APPLICATION_JSON)

                .body(projectService.findProjectByNameAndCostQuery(name, Long.valueOf(cost)), Project.class).log();
    }

    public Mono<ServerResponse> findByEstimatedCostBetweenQuery(ServerRequest request) {
        String from = request.queryParam("from").get();
        String to = request.queryParam("to").get();
        return ok()

                .contentType(MediaType.APPLICATION_JSON)

                .body(projectService.findByEstimatedCostBetweenQuery(Long.valueOf(from), Long.valueOf(to)), Project.class).log();
    }


    public Mono<ServerResponse> findByNameRegexQuery(ServerRequest request) {
        String name = request.queryParam("name").get();
        String regex = "^" + name + "";
        return ok()

                .contentType(MediaType.APPLICATION_JSON)

                .body(projectService.findByNameRegexQuery(regex), Project.class).log();
    }


    public Mono<ServerResponse> findProjectByNameQueryWithTemplate(ServerRequest request) {
        String name = request.queryParam("name").get();
        return ok()

                .contentType(MediaType.APPLICATION_JSON)

                .body(projectService.findProjectByNameQueryWithTemplate(name), Project.class).log();
    }


    public Mono<ServerResponse> findByEstimatedCostBetweenQueryWithTemplate(ServerRequest request) {
        String from = request.queryParam("from").get();
        String to = request.queryParam("to").get();
        return ok()

                .contentType(MediaType.APPLICATION_JSON)

                .body(projectService.findByEstimatedCostBetweenQueryWithTemplate(Long.parseLong(from), Long.parseLong(to)), Project.class).log();


    }

    public Mono<ServerResponse> findByNameRegexQueryWithTemplate(ServerRequest request) {
        String name = request.queryParam("name").get();
        String regex = "^" + name + "";
        return ok()

                .contentType(MediaType.APPLICATION_JSON)

                .body(projectService.findByNameRegexQueryWithTemplate(regex), Project.class).log();


    }

    public Mono<ServerResponse> upsertCostWithCriteriaTemplate(ServerRequest request) {
        String id = request.queryParam("id").get();
        String cost = request.queryParam("cost").get();
        return ok()

                .contentType(MediaType.APPLICATION_JSON)

                .body(projectService.upsertCostWithCriteriaTemplate(id, Long.valueOf(cost)), Void.class).log();


    }

    public Mono<ServerResponse> deleteWithCriteriaTemplate(ServerRequest request) {
        String id = request.queryParam("id").get();

        return ok()

                .contentType(MediaType.APPLICATION_JSON)

                .body(projectService.deleteWithCriteriaTemplate(id), Void.class).log();


    }

    public Mono<ServerResponse> findNoOfProjectsCostGreaterThan(ServerRequest request) {
        String cost = request.queryParam("cost").get();
        return ok()

                .contentType(MediaType.APPLICATION_JSON)

                .body(projectService.findNoOfProjectsCostGreaterThan(Long.valueOf(cost)), Long.class).log();

    }

    public Mono<ServerResponse> findCostsGroupByStartDateForProjectsCostGreaterThan(ServerRequest request) {
        String cost = request.queryParam("cost").get();
        return ok()

                .contentType(MediaType.APPLICATION_JSON)

                .body(projectService.findCostsGroupByStartDateForProjectsCostGreaterThan(Long.valueOf(cost)), ResultByStartDateAndCost.class).log();

    }


    public Mono<ServerResponse> findAllProjectTasks(ServerRequest request) {

        return ok()

                .contentType(MediaType.APPLICATION_JSON)

                .body(projectService.findAllProjectTasks(), ResultProjectTasks.class).log();

    }


    public Mono<ServerResponse> saveProjectAndTask(ServerRequest request) {

        Project p = new Project();
        p.set_id("6");
        p.setName("Project6");

        Task t = new Task();
        t.set_id("10");
        t.setProjectId("6");


        return ok()

                .contentType(MediaType.APPLICATION_JSON)

                .body(projectService.saveProjectAndTask(Mono.just(p), Mono.just(t)), Void.class).log();
    }

    public Mono<ServerResponse> chunkAndSaveProject(ServerRequest request) {

        Project p = new Project();
        p.set_id("20");
        p.setName("ProjectGrid");

        return ok()

                .contentType(MediaType.APPLICATION_JSON)

                .body(projectService.chunkAndSaveProject(p), Void.class).log();
    }


    public Mono<ServerResponse> loadProjectFromGrid(ServerRequest request) {
        String pid = request.queryParam("pid").get();


        return ok()

                .contentType(MediaType.APPLICATION_JSON)

                .body(projectService.loadProjectFromGrid(pid), Project.class).log();
    }


    public Mono<ServerResponse> deleteProjectFromGrid(ServerRequest request) {
        String pid = request.queryParam("pid").get();


        return ok()

                .contentType(MediaType.APPLICATION_JSON)

                .body(projectService.deleteProjectFromGrid(pid), Project.class).log();
    }

    public Mono<ServerResponse> findNameDescriptionForMatchingTerm(ServerRequest serverRequest) {
        String term = serverRequest.queryParam("term").get();
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findNameDescriptionForMatchingTerm(term), Project.class).log();
    }

    public Mono<ServerResponse> findNameDescriptionForMatchingAny(ServerRequest serverRequest) {
        Mono<String[]> words = serverRequest.bodyToMono(String[].class);
        Flux<Project> p = words.flatMapMany(projectService::findNameDescriptionForMatchingAny);
        return ok().contentType(MediaType.APPLICATION_JSON).body(p, Project.class).log();
    }

}
