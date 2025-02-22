package wit.books_store.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import wit.books_store.Mapper;
import wit.books_store.dto.BookDto;
import wit.books_store.exceptions.NotFoundException;
import wit.books_store.models.Book;
import wit.books_store.repository.BookRepository;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BookService {
    private final BookRepository repository;

    public List<BookDto> findAll(Pageable pageable) {
        log.info("show all books");
        return repository.findAll(pageable).stream().map(Mapper::toBookDto).toList();
    }

    public BookDto getById(long id) {
        Book book = repository.findById(id).orElseThrow(() -> new NotFoundException(""));
        log.info("found the book with id {}", id);
        return Mapper.toBookDto(book);
    }

    public void create(Book book) {
        repository.save(book);
        log.info("new book was created");
    }

    public List<BookDto> findBooksByDate(OffsetDateTime startOfDay, OffsetDateTime endOfDay) {
        return repository.findBooksByDate(startOfDay, endOfDay).stream().map(Mapper::toBookDto).toList();
    }
}
