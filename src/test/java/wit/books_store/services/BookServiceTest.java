package wit.books_store.services;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import wit.books_store.exceptions.NotFoundException;
import wit.books_store.models.Book;
import wit.books_store.repository.BookRepository;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository repository;

    @InjectMocks
    private BookService service;

    Book book0;
    Book book1;

    @BeforeEach
    void createBooks() {
        book0 =  Book.builder()
                .book_id(0L)
                .title("A rabbit")
                .author("J. Dan")
                .price(579)
                .isPresent(true)
                .build();

        book1 =  Book.builder()
                .book_id(1L)
                .title("Harry Potter")
                .author("J.K. Rowling")
                .price(1599)
                .isPresent(false)
                .build();

    }


    @Test
    void shouldReturnBookIfExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(book0));
        Book foundBook = service.getById(1L);
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
        LocalDate date = LocalDate.now();
        OffsetDateTime startDate = date.atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
        OffsetDateTime endDay = startDate.plusDays(1).minusNanos(1);
        when(repository.findBooksByDate(startDate, endDay)).thenReturn(List.of(book0, book1));
        service.findBooksByDate(date);
        verify(repository).findBooksByDate(startDate, endDay);
    }

    @Test
    void shouldCreateBook() {
        service.create(book0);
        verify(repository).save(book0);
    }
}
