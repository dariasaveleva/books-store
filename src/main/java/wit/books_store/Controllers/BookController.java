package wit.books_store.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wit.books_store.models.Book;
import wit.books_store.services.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController (BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/all")
    public List<Book> getAll() {
        return bookService.findAll();
    }

    @GetMapping
    public Book getById(@RequestParam Long id) {
        return bookService.getById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public String createBook(@RequestBody Book book) {
        bookService.create(book);
        return "the book was added";
    }
}
