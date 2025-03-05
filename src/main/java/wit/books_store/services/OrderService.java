package wit.books_store.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import wit.books_store.exceptions.NotFoundException;
import wit.books_store.exceptions.ValidationException;
import wit.books_store.models.Book;
import wit.books_store.models.Order;
import wit.books_store.repository.BookRepository;
import wit.books_store.repository.CustomerRepository;
import wit.books_store.repository.OrderRepository;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository repository;
    private final BookRepository bookRepository;
    private final CustomerRepository customerRepository;

    public List<Order> findAll(Pageable pageable) {
        log.info("show all customers");
        return repository.findAll(pageable);
    }

    public Order getById(long id) {
        Order order = repository.findById(id).orElseThrow(() -> new NotFoundException("order does not exist"));
        log.info("found the order with id {}", id);
        return order;
    }

    public void create(Order order) {
        if (isOrderValid(order.getCustomerId(), order.getBooks())) {
            order.setCreatedDate(OffsetDateTime.now());
            order.setSum(countSum(order.getBooks()));
            repository.save(order);
            log.info("new order was created");
        } else {
            throw new ValidationException("cannot create order with invalid data: customer or books don't exist");
        }
    }

    public double countSum(List<Long> booksIds) {
        List<Book> books = bookRepository.findBooksByIds(booksIds);
        return books.stream()
                .mapToDouble(Book::getPrice)
                .sum();
    }

    private boolean isOrderValid(long id, List<Long> ids) {
        return customerRepository.findById(id).isPresent() &&
                !bookRepository.findBooksByIds(ids).isEmpty();
    }
}
