package wit.books_store.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wit.books_store.models.Book;
import wit.books_store.repository.BookRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository repository;

    public List<Book> findAll() {
        log.info("show all books");
        return repository.findAll();
    }

    public Book getById(Long id) {
            Book book = repository.findById(id);
            log.info("found the book with id {}", id);
            return book;
    }

    @Transactional()
    public void create(Book book) {
        repository.save(book);
        log.info("new book was created");
    }

    public List<Book> findBooksByDate(LocalDate date) {
        return repository.findBooksByDate(date);
    }
}
