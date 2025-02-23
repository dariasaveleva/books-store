package wit.books_store.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wit.books_store.Mapper;
import wit.books_store.dto.BookDto;
import wit.books_store.services.BookService;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/all")
    public List<BookDto> getAll(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                @PositiveOrZero @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(from/size, size);
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public BookDto getById(@PathVariable long id) {
        return bookService.getById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public String createBook(@Valid @RequestBody BookDto bookDto) {
        bookService.create(Mapper.toBook(bookDto));
        return "the book was added";
    }

    @GetMapping("/statistics")
    public List<BookDto> getBooksByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        OffsetDateTime startOfDay = date.atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
        OffsetDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
        return bookService.findBooksByDate(startOfDay, endOfDay);
    }
}
