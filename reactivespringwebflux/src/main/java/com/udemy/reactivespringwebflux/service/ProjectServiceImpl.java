package com.udemy.reactivespringwebflux.service;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.CountOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsOperations;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.primitives.Bytes;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.udemy.reactivespringwebflux.model.Project;
import com.udemy.reactivespringwebflux.model.Task;
import com.udemy.reactivespringwebflux.repository.ProjectRepository;
import com.udemy.reactivespringwebflux.repository.TaskRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ReactiveGridFsTemplate reactiveGridFsTemplate;
    @Autowired
    private ReactiveGridFsOperations reactiveGridFsOperations;
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Mono<Project> createProject(Project project) {

        return projectRepository.save(project);
    }

    @Override
    public Mono<Task> createTask(Task task) {
        // TODO Auto-generated method stub
        return taskRepository.save(task);
    }

    @Override
    public Flux<Project> findAll() {
        // TODO Auto-generated method stub
        return projectRepository.findAll();
    }

    @Override
    public Mono<Project> findById(String id) {
        // TODO Auto-generated method stub
        return projectRepository.findById(id);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        // TODO Auto-generated method stub
        return projectRepository.deleteById(id);
    }

    @Override
    public Flux<Project> findByName(String name) {
        // TODO Auto-generated method stub
        return projectRepository.findByName(name);
    }

    @Override
    public Flux<Project> findByNameNot(String name) {
        // TODO Auto-generated method stub
        return projectRepository.findByNameNot(name);
    }

    @Override
    public Flux<Project> findByEstimatedCostGreaterThan(Long cost) {
        // TODO Auto-generated method stub
        return projectRepository.findByEstimatedCostGreaterThan(cost);
    }

    @Override
    public Flux<Project> findByEstimatedCostBetween(Long from, Long to) {
        // TODO Auto-generated method stub
        return projectRepository.findByEstimatedCostBetween(from, to);
    }

    @Override
    public Flux<Project> findByNameLike(String name) {
        // TODO Auto-generated method stub
        return projectRepository.findByNameLike(name);
    }

    @Override
    public Flux<Project> findByNameRegex(String name) {
        // TODO Auto-generated method stub
        return projectRepository.findByNameRegex(name);
    }

    @Override
    public Flux<Project> findProjectByNameQuery(String name) {
        // TODO Auto-generated method stub
        return projectRepository.findProjectByNameQuery(name);
    }

    @Override
    public Flux<Project> findProjectByNameAndCostQuery(String name, Long cost) {
        // TODO Auto-generated method stub
        return projectRepository.findProjectByNameAndCostQuery(name, cost);
    }

    @Override
    public Flux<Project> findByEstimatedCostBetweenQuery(Long from, Long to) {
        // TODO Auto-generated method stub
        return projectRepository.findByEstimatedCostBetweenQuery(from, to,
                org.springframework.data.domain.Sort.by(Direction.DESC, "cost"));
    }

    @Override
    public Flux<Project> findByNameRegexQuery(String regexp) {
        // TODO Auto-generated method stub
        return projectRepository.findByNameRegexQuery(regexp);
    }

    @Override
    public Flux<Project> findProjectByNameQueryWithTemplate(String name) {

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        return reactiveMongoTemplate.find(query, Project.class);

    }

    @Override
    public Flux<Project> findByEstimatedCostBetweenQueryWithTemplate(Long from, Long to) {

        Query query = new Query();
        query.with(org.springframework.data.domain.Sort.by(Direction.ASC, "cost"));
        query.addCriteria(Criteria.where("cost").lt(to).gt(from));
        return reactiveMongoTemplate.find(query, Project.class);

    }

    @Override
    public Flux<Project> findByNameRegexQueryWithTemplate(String regexp) {

        Query query = new Query();
        query.addCriteria(Criteria.where("name").regex(regexp));
        return reactiveMongoTemplate.find(query, Project.class);

    }

    @Override
    public Mono<Void> upsertCostWithCriteriaTemplate(String id, Long cost) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.set("cost", cost);
        return reactiveMongoTemplate.upsert(query, update, Project.class).then();

    }

    @Override
    public Mono<Void> deleteWithCriteriaTemplate(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        return reactiveMongoTemplate.remove(query, Project.class).then();

    }

    /**
     * db.project.aggregate([{$match:{"cost" : {"$gt" : 2000}}},{ $count:
     * "costly_projects" }]);
     */

    @Override
    public Mono<Long> findNoOfProjectsCostGreaterThan(Long cost) {
        // TODO Auto-generated method stub
        MatchOperation matchStage = Aggregation.match(new Criteria("cost").gt(cost));
        CountOperation countStage = Aggregation.count().as("costly_projects");

        Aggregation aggregation = Aggregation.newAggregation(matchStage, countStage);
        Flux<ResultCount> output = reactiveMongoTemplate.aggregate(aggregation, "project", ResultCount.class);
        Flux<Long> resultc = output.map(result -> result.getCostly_projects()).switchIfEmpty(Flux.just(0L));
        return resultc.take(1).single();

    }

    /**
     * db.project.aggregate([ {$match:{"cost" : {"$gt" : 100}}}, {$group: {
     * _id:"$startDate",total:{$sum:"$cost"} } }, {$sort: { "total": -1 } } ]);
     */
    @Override
    public Flux<ResultByStartDateAndCost> findCostsGroupByStartDateForProjectsCostGreaterThan(Long cost) {

        MatchOperation filterCost = Aggregation.match(new Criteria("cost").gt(cost));
        GroupOperation groupByStartDateAndSumCost = Aggregation.group("startDate").sum("cost").as("total");

        SortOperation sortByTotal = Aggregation.sort(org.springframework.data.domain.Sort.by(Direction.DESC, "total"));

        Aggregation aggregation = Aggregation.newAggregation(filterCost, groupByStartDateAndSumCost, sortByTotal);
        return reactiveMongoTemplate.aggregate(aggregation, "project", ResultByStartDateAndCost.class);

    }

    @Override
    public Flux<ResultProjectTasks> findAllProjectTasks() {

        LookupOperation lookupOperation = LookupOperation.newLookup().from("task").localField("_id").foreignField("pid")
                .as("ProjectTasks");
        UnwindOperation unwindOperation = Aggregation.unwind("ProjectTasks");
        ProjectionOperation projectOpertaion = Aggregation.project().andExpression("_id").as("_id")
                .andExpression("name").as("name").andExpression("ProjectTasks.name").as("taskName")
                .andExpression("ProjectTasks.ownername").as("taskOwnerName");
        Aggregation aggregation = Aggregation.newAggregation(lookupOperation, unwindOperation, projectOpertaion);
        return reactiveMongoTemplate.aggregate(aggregation, "project", ResultProjectTasks.class);

    }

    @Override
    @Transactional
    public Mono<Void> saveProjectAndTask(Mono<Project> p, Mono<Task> t) {

        return p.flatMap(projectRepository::save).then(t).flatMap(taskRepository::save).then();

    }

    @Override
    public Mono<Void> chunkAndSaveProject(Project p) {
        String s = serializetoJson(p);
        byte[] serialized = s.getBytes();

        DBObject metaData = new BasicDBObject();
        metaData.put("projectId", p.get_id());
        DefaultDataBufferFactory factory = new DefaultDataBufferFactory();
        DefaultDataBuffer dataBuffer =
                factory.wrap(serialized);
        Flux<DataBuffer> body = Flux.just(dataBuffer);
        return reactiveGridFsTemplate.store(body, p.get_id(), metaData).then();

    }


    @Override
    public Mono<Project> loadProjectFromGrid(String projectId) {
        Mono<GridFSFile> file = reactiveGridFsTemplate.findOne(new Query(Criteria.where("metadata.projectId").is(projectId)).with(Sort.by(Sort.Direction.DESC, "uploadDate")).limit(1));

        Flux<byte[]> bytesseq = file.flatMap(f -> reactiveGridFsOperations.getResource(f)).flatMapMany(rgrs -> rgrs.getDownloadStream()).map(buffer -> {

            byte[] b = new byte[buffer.readableByteCount()];
            buffer.read(b);
            return b;
        });


        Mono<Project> totalbyte = bytesseq.collectList().flatMap(bytes -> {
            byte[] data = Bytes.concat(bytes.toArray(new byte[bytes.size()][]));
            String s = new String(data, StandardCharsets.UTF_8);

            return Mono.just(deserialize(s));
        });

        return totalbyte;
    }

    @Override
    public Mono<Void> deleteProjectFromGrid(String projectId) {
        return reactiveGridFsTemplate.delete(new Query(Criteria.where("metadata.projectId").is(projectId)));
    }

    @Override
    public Flux<Project> findNameDescriptionForMatchingTerm(String term) {
        Query query = TextQuery.queryText(TextCriteria.forDefaultLanguage().matching(term)).sortByScore()
                .with(Sort.by(Sort.Direction.DESC, "score"));
        return reactiveMongoTemplate.find(query, Project.class);
    }

    @Override
    public Flux<Project> findNameDescriptionForMatchingAny(String... words) {
        Query query = TextQuery.queryText(TextCriteria.forDefaultLanguage().matchingAny(words)).sortByScore().with(Sort.by(Sort.Direction.DESC, "score"));
        return reactiveMongoTemplate.find(query, Project.class);
    }

    private String serializetoJson(Project p) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(p);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Project deserialize(String json) {

        Project p = null;
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            p = objectMapper.readValue(json, Project.class);
        } catch (Exception i) {

            throw new RuntimeException(i);
        }
        return p;
    }

}
