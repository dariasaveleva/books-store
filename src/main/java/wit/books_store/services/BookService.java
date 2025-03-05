package wit.books_store.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import wit.books_store.exceptions.NotFoundException;
import wit.books_store.models.Book;
import wit.books_store.repository.BookRepository;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BookService {
    private final BookRepository repository;

    public List<Book> findAll(Pageable pageable) {
        log.info("show all books");
        return repository.findAll(pageable);
    }

    public Book getById(long id) {
        Book book = repository.findById(id).orElseThrow(() -> new NotFoundException(""));
        log.info("found the book with id {}", id);
        return book;
    }

    public Book create(Book book) {
        Book book1 = repository.save(book);
        log.info("new book was created");
        return book1;
    }

    public List<Book> findBooksByDate(LocalDate date) {
        OffsetDateTime startOfDay = date.atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
        OffsetDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
        return repository.findBooksByDate(startOfDay, endOfDay);
    }
}
