package com.murilodias03.bookstore.repositories;

import com.murilodias03.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}