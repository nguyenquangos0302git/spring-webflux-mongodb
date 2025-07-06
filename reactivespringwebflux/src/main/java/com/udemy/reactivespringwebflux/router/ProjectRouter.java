package com.udemy.reactivespringwebflux.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.udemy.reactivespringwebflux.handler.ProjectHandler;

@Configuration
public class ProjectRouter {
    @Bean
    public RouterFunction<ServerResponse> routeProjects(ProjectHandler handler) {
        return RouterFunctions
                .route(RequestPredicates.POST("/project/create")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::createProject)
                .andRoute(RequestPredicates.POST("/project/createTask")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::createTask)
                .andRoute(RequestPredicates.GET("/project")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findAll)
                .andRoute(RequestPredicates.GET("/project/{id}")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findById)
                .andRoute(RequestPredicates.DELETE("/project/{id}")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::delete)


                .andRoute(RequestPredicates.GET("/project/find/ByName")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findByName)
                .andRoute(RequestPredicates.GET("/project/find/ByNameNot")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findByNameNot)
                .andRoute(RequestPredicates.GET("/project/find/ByEstimatedCostGreaterThan")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findByEstimatedCostGreaterThan)
                .andRoute(RequestPredicates.GET("/project/find/ByEstimatedCostBetween")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findByEstimatedCostBetween)
                .andRoute(RequestPredicates.GET("/project/find/ByNameLike")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findByNameLike)
                .andRoute(RequestPredicates.GET("/project/find/ByNameRegex")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findByNameRegex)


                .andRoute(RequestPredicates.GET("/project/query/ByName")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findByNameQuery)
                .andRoute(RequestPredicates.GET("/project/query/ByNameAndCostQuery")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findByNameAndCostQuery)
                .andRoute(RequestPredicates.GET("/project/query/ByEstimatedCostBetween")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findByEstimatedCostBetweenQuery)
                .andRoute(RequestPredicates.GET("/project/query/ByNameRegex")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findByNameRegexQuery)

                .andRoute(RequestPredicates.GET("/project/template/ByNameQuery")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findProjectByNameQueryWithTemplate)
                .andRoute(RequestPredicates.GET("/project/template/ByEstimatedCostBetweenQuery")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findByEstimatedCostBetweenQueryWithTemplate)
                .andRoute(RequestPredicates.GET("/project/template/ByNameRegexQuery")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findByNameRegexQueryWithTemplate)
                .andRoute(RequestPredicates.POST("/project/template/upsertCostWithCriteria")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::upsertCostWithCriteriaTemplate)
                .andRoute(RequestPredicates.DELETE("/project/template/deleteWithCriteria")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::deleteWithCriteriaTemplate)

                .andRoute(RequestPredicates.GET("/project/template/aggregate/findNoOfProjectsCostGreaterThan")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findNoOfProjectsCostGreaterThan)
                .andRoute(RequestPredicates.GET("/project/template/aggregate/findCostsGroupByStartDateForProjectsCostGreaterThan")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findCostsGroupByStartDateForProjectsCostGreaterThan)

                .andRoute(RequestPredicates.GET("/project/template/aggregate/findAllProjectTasks")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findAllProjectTasks)
                .andRoute(RequestPredicates.POST("/project/savewithtx")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::saveProjectAndTask)

                .andRoute(RequestPredicates.POST("/project/grid/chunkAndSave")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::chunkAndSaveProject)
                .andRoute(RequestPredicates.GET("/project/grid/load")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::loadProjectFromGrid)
                .andRoute(RequestPredicates.DELETE("/project/grid/delete")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::deleteProjectFromGrid)
                .andRoute(RequestPredicates.GET("/project/template/fts/findNameDescriptionForMatchingTerm")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findNameDescriptionForMatchingTerm)
                .andRoute(RequestPredicates.POST("/project/template/fts/findNameDescriptionForMatchingAny")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findNameDescriptionForMatchingAny);

    }
}
