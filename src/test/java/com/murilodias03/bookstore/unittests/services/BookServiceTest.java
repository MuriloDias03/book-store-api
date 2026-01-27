package com.murilodias03.bookstore.unittests.services;

import com.murilodias03.bookstore.data.dto.BookDTO;
import com.murilodias03.bookstore.exceptions.RequeriedObjectsIsNullException;
import com.murilodias03.bookstore.model.Book;
import com.murilodias03.bookstore.repositories.BookRepository;
import com.murilodias03.bookstore.services.BookService;
import com.murilodias03.bookstore.unittests.mapper.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    MockBook input;

    @InjectMocks
    private BookService service;

    @Mock
    BookRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockBook();
    }

    @Test
    void findById() {

        Book book = input.mockEntity(1);
        book.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        var result = service.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("self")
                    && link.getHref().endsWith("/books/1")
                    && link.getType().equals("GET")
            ));

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("findAll")
                    && link.getHref().endsWith("/books")
                    && link.getType().equals("GET")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("create")
                    && link.getHref().endsWith("/books")
                    && link.getType().equals("POST")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("update")
                    && link.getHref().endsWith("/books")
                    && link.getType().equals("PUT")
            )
        );

        assertNotNull(result.getLinks().stream()
            .anyMatch(link -> link.getRel().value().equals("delete")
                    && link.getHref().endsWith("/books/1")
                    && link.getType().equals("DELETE")
            )
        );

        assertEquals("Some Author1", result.getAuthor());
        assertEquals(new BigDecimal("100.0"), result.getPrice());
        assertEquals("Some Title1", result.getTitle());
        assertNotNull(result.getLaunchDate());
    }

    @Test
    void create() {
        Book book = input.mockEntity(1);
        Book persisted = book;
        persisted.setId(1L);

        BookDTO dto = input.mockDTO(1);

        when(repository.save(any(Book.class))).thenReturn(persisted);

        var result = service.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/books/1")
                        && link.getType().equals("GET")
                ));

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("GET")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("POST")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("PUT")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/books/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Some Author1", result.getAuthor());
        assertEquals(new BigDecimal("100.0"), result.getPrice());
        assertEquals("Some Title1", result.getTitle());
        assertNotNull(result.getLaunchDate());
    }

    @Test
    void testCreateWithNullBook() {
        Exception exception = assertThrows(RequeriedObjectsIsNullException.class,
        () -> {
            service.create(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        Book book = input.mockEntity(1);
        Book persisted = book;
        persisted.setId(1L);

        BookDTO dto = input.mockDTO(1);

        when(repository.findById(1L)).thenReturn(Optional.of(book));
        when(repository.save(book)).thenReturn(persisted);

        var result = service.update(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/books/1")
                        && link.getType().equals("GET")
                ));

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("GET")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("POST")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("PUT")
                )
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/books/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Some Author1", result.getAuthor());
        assertEquals(new BigDecimal("100.0"), result.getPrice());
        assertEquals("Some Title1", result.getTitle());
        assertNotNull(result.getLaunchDate());
    }

    @Test
    void testUpdateWithNullBook() {
        Exception exception = assertThrows(RequeriedObjectsIsNullException.class,
                () -> {
                    service.update(null);
                });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete() {
        Book book = input.mockEntity(1);
        book.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        service.delete(1L);
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Book.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findAll() {
        List<Book> list = input.mockEntityList();
        when(repository.findAll()).thenReturn(list);
        List<BookDTO> books = service.findAll();

        assertNotNull(books);
        assertEquals(14, books.size());

        var book1 = books.get(1);

        assertNotNull(book1);
        assertNotNull(book1.getId());
        assertNotNull(book1.getLinks());

        assertNotNull(book1.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/books/1")
                        && link.getType().equals("GET")
                ));

        assertNotNull(book1.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("GET")
                )
        );

        assertNotNull(book1.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("POST")
                )
        );

        assertNotNull(book1.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("PUT")
                )
        );

        assertNotNull(book1.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/books/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Some Author1", book1.getAuthor());
        assertEquals(new BigDecimal("100.0"), book1.getPrice());
        assertEquals("Some Title1", book1.getTitle());
        assertNotNull(book1.getLaunchDate());

        var book4 = books.get(4);

        assertNotNull(book4);
        assertNotNull(book4.getId());
        assertNotNull(book4.getLinks());

        assertNotNull(book4.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/books/1")
                        && link.getType().equals("GET")
                ));

        assertNotNull(book4.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("GET")
                )
        );

        assertNotNull(book4.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("POST")
                )
        );

        assertNotNull(book4.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("PUT")
                )
        );

        assertNotNull(book4.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/books/1")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Some Author4", book4.getAuthor());
        assertEquals(new BigDecimal("100.0"), book4.getPrice());
        assertEquals("Some Title4", book4.getTitle());
        assertNotNull(book4.getLaunchDate());

        var book7 = books.get(7);

        assertNotNull(book7);
        assertNotNull(book7.getId());
        assertNotNull(book7.getLinks());

        assertNotNull(book7.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/books/7")
                        && link.getType().equals("GET")
                ));

        assertNotNull(book7.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("GET")
                )
        );

        assertNotNull(book7.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("POST")
                )
        );

        assertNotNull(book7.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/books")
                        && link.getType().equals("PUT")
                )
        );

        assertNotNull(book7.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/books/7")
                        && link.getType().equals("DELETE")
                )
        );

        assertEquals("Some Author7", book7.getAuthor());
        assertEquals(new BigDecimal("100.0"), book7.getPrice());
        assertEquals("Some Title7", book7.getTitle());
        assertNotNull(book7.getLaunchDate());
    }
}