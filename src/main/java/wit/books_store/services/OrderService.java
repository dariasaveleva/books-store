package wit.books_store.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wit.books_store.models.Book;
import wit.books_store.models.Order;
import wit.books_store.repository.BookRepository;
import wit.books_store.repository.OrderRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository repository;
    private final BookRepository bookRepository;

    public List<Order> findAll() {
        log.info("show all customers");
        return repository.findAll();
    }

    public Order getById(Long id) {
        Order order = repository.findById(id);
        log.info("found the order with id {}", id);
        return order;
    }

    @Transactional()
    public void create(Order order) {
        order.setCreatedDate(LocalDate.now());
        order.setSum(countSum(order.getBooks()));
        repository.save(order);
        log.info("new order was created");
    }

    private double countSum(List<Long> booksIds) {
        List<Book> books = bookRepository.findBooksByIds(booksIds);
        return books.stream()
                .mapToDouble(Book::getPrice)
                .sum();
    }
}
