package com.dogukano.springbootgraphqlbookshop.service.datafetcher;

import com.dogukano.springbootgraphqlbookshop.model.Book;
import com.dogukano.springbootgraphqlbookshop.repository.BookRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AllBooksFetcher implements DataFetcher<List<Book>> {

    @Autowired
    BookRepository bookRepository;

    @Override
    public List<Book> get(DataFetchingEnvironment dataFetchingEnvironment) {
        return bookRepository.findAll();
    }
}
