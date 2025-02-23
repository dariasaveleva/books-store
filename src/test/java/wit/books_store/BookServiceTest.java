package wit.books_store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import wit.books_store.dto.BookDto;
import wit.books_store.exceptions.NotFoundException;
import wit.books_store.models.Book;
import wit.books_store.repository.BookRepository;
import wit.books_store.services.BookService;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository repository;

    @InjectMocks
    private BookService service;

    Book book0;
    Book book1;

    @BeforeEach
    void createBooks() {
        book0 = new Book(0L, "A rabbit", "J. Dan", 579, true);
        book1 = new Book(1L, "Harry Potter", "J.K. Rowling", 1599, false);
    }


    @Test
    void shouldReturnBookIfExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(book0));
        BookDto foundBook = service.getById(1L);
        assertEquals("A rabbit", foundBook.getTitle());
        verify(repository).findById(1L);
    }

    @Test
    void shouldReturnErrorIfBookNotExist() {
        BookService bookService = mock(BookService.class);
        when(bookService.getById(10L)).thenThrow(new NotFoundException("Book is not found"));
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> bookService.getById(10L));
        assertEquals("Book is not found", thrown.getMessage());
    }

    @Test
    void shouldReturnListOfBooks() {
        Pageable pageable = PageRequest.of(1, 10);
        when(repository.findAll(pageable)).thenReturn(List.of(book0, book1));
        service.findAll(pageable);
        verify(repository).findAll(pageable);
    }

    @Test
    void shouldReturnBooksByDate() {
        OffsetDateTime startDate = OffsetDateTime.parse("2025-02-28T00:00:00+03:00");
        OffsetDateTime endDay = startDate.plusDays(1).minusNanos(1);
        when(repository.findBooksByDate(startDate, endDay)).thenReturn(List.of(book0, book1));
        service.findBooksByDate(startDate, endDay);
        verify(repository).findBooksByDate(startDate, endDay);
    }

    @Test
    void shouldCreateBook() {
        service.create(Mapper.toBookDto(book0));
        verify(repository).save(book0);
    }
}
