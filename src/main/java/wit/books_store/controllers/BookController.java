package wit.books_store.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wit.books_store.dto.BookDto;
import wit.books_store.mappers.BookMapper;
import wit.books_store.models.Book;
import wit.books_store.services.BookService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;


    @GetMapping("/all")
    public List<BookDto> getAll(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                @PositiveOrZero @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(from/size, size);
        return bookService.findAll(pageable).stream().map(bookMapper::toBookDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BookDto getById(@PathVariable long id) {
        return bookMapper.toBookDto(bookService.getById(id));
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto createBook(@Valid @RequestBody BookDto bookDto) {
        Book book = bookService.create(bookMapper.toBook(bookDto));
        return bookMapper.toBookDto(book);
    }

    @GetMapping("/statistics")
    public List<BookDto> getBooksByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return bookService.findBooksByDate(date).stream().map(bookMapper::toBookDto).collect(Collectors.toList());
    }
}
