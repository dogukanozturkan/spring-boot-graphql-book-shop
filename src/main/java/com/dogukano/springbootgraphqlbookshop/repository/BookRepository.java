package com.dogukano.springbootgraphqlbookshop.repository;

import com.dogukano.springbootgraphqlbookshop.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {

}
