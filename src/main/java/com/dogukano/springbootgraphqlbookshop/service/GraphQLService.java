package com.dogukano.springbootgraphqlbookshop.service;

import com.dogukano.springbootgraphqlbookshop.model.Book;
import com.dogukano.springbootgraphqlbookshop.repository.BookRepository;
import com.dogukano.springbootgraphqlbookshop.service.datafetcher.AllBooksFetcher;
import com.dogukano.springbootgraphqlbookshop.service.datafetcher.BookDataFetcher;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.io.File;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class GraphQLService {

    @Value("classpath:books.graphql")
    Resource resource;

    private GraphQL graphQL;

    @Autowired
    private AllBooksFetcher allBooksDataFetcher;

    @Autowired
    private BookDataFetcher bookDataFetcher;

    @Autowired
    private BookRepository bookRepository;

    @SneakyThrows
    @PostConstruct
    private void setResource() {


        // Load Book Data to BookRepository
        loadDataIntoHSQL();

        // Get GraphQL Schema
        File schemaFile = resource.getFile();

        // Parse Schema
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaFile);

        RuntimeWiring wiring = buildRuntimeWiring();

        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);

        graphQL = GraphQL.newGraphQL(schema).build();

    }

    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring ->
                        typeWiring
                                .dataFetcher("allBooks", allBooksDataFetcher)
                                .dataFetcher("book", bookDataFetcher)
                )
                .build();
    }

    private void loadDataIntoHSQL() {

        Stream.of(
                new Book("0001",
                        "CreateSpace Independent Publishing Platform",
                        "Cloud Computing: From Beginning to End",
                        new String[] {"Mr. Ray J Rafaels"},
                        "April 1, 2015"),
                new Book("0002",
                        "Architecting the Cloud: Design Decisions for Cloud Computing",
                        "Wiley; 1 edition",
                        new String[] {"Michael J. Kavis"},
                        "January 28, 2014"),
                new Book("0003",
                        "Cloudonomics, + Website: The Business Value of Cloud Computing",
                        "Wiley; 1 edition",
                        new String[] {"Joe Weinman"},
                        "September 4, 2012")
        ).forEach(book -> {
            bookRepository.save(book);
        });
    }

    public GraphQL getGraphQL() {
        return graphQL;
    }
}
