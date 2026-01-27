package com.murilodias03.bookstore.services;

import com.murilodias03.bookstore.controllers.BookController;
import com.murilodias03.bookstore.data.dto.BookDTO;
import com.murilodias03.bookstore.exceptions.RequeriedObjectsIsNullException;
import com.murilodias03.bookstore.exceptions.ResourceNotFoundException;
import com.murilodias03.bookstore.model.Book;
import com.murilodias03.bookstore.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static com.murilodias03.bookstore.mapper.ObjectMapper.parseListObjects;
import static com.murilodias03.bookstore.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    private final Logger logger = Logger.getLogger(BookService.class.getName());
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookDTO> findAll() {
        logger.info("Finding all books!");

        var books = parseListObjects(bookRepository.findAll(), BookDTO.class);

        books.forEach(this::addHateoasLinks);

        return books;
    }

    public BookDTO findById(Long id) {
        logger.info("Finding one book!");

        var entity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        var dto = parseObject(entity, BookDTO.class);

        addHateoasLinks(dto);

        return dto;
    }

    public BookDTO create(BookDTO bookDTO) {

        if (bookDTO == null) throw new RequeriedObjectsIsNullException();

        logger.info("Creating one bookDTO!");

        var entity = parseObject(bookDTO, Book.class);

        var dto = parseObject(bookRepository.save(entity), BookDTO.class);

        addHateoasLinks(dto);

        return dto;
    }

    public BookDTO update(BookDTO bookDTO) {

        if (bookDTO == null) throw new RequeriedObjectsIsNullException();

        logger.info("Updating one book!");

        var entity = bookRepository.findById(bookDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setAuthor(bookDTO.getAuthor());
        entity.setLaunchDate(bookDTO.getLaunchDate());
        entity.setPrice(bookDTO.getPrice());
        entity.setTitle(bookDTO.getTitle());

        var dto = parseObject(bookRepository.save(entity), BookDTO.class);

        addHateoasLinks(dto);

        return dto;
    }

    public void delete(Long id) {
        logger.info("Deleting one book!");

        var entity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        bookRepository.delete(entity);
    }

    private void addHateoasLinks(BookDTO dto) {
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }

}