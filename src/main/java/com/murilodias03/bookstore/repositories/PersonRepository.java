package com.murilodias03.bookstore.repositories;

import com.murilodias03.bookstore.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}