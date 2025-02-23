package wit.books_store;

import wit.books_store.dto.BookDto;
import wit.books_store.dto.CustomerDto;
import wit.books_store.dto.OrderDto;
import wit.books_store.models.Book;
import wit.books_store.models.Customer;
import wit.books_store.models.Order;

public class Mapper {

    public static Book toBook(BookDto bookDto) {
        return new Book(
                bookDto.getId(),
                bookDto.getTitle(),
                bookDto.getAuthor(),
                bookDto.getPrice(),
                bookDto.isPresent()
        );
    }

    public static BookDto toBookDto(Book book) {
        return new BookDto(
                book.getBook_id(),
                book.getTitle(),
                book.getAuthor(),
                book.getPrice(),
                book.isPresent()
        );
    }

    public static Customer toCustomer(CustomerDto customerDto) {
        return new Customer(
                customerDto.getCustomer_id(),
                customerDto.getName(),
                customerDto.getSurname(),
                customerDto.getPhone(),
                customerDto.getPhone()
        );
    }

    public static CustomerDto toCustomerDto(Customer customer) {
        return new CustomerDto(
                customer.getCustomer_id(),
                customer.getName(),
                customer.getSurname(),
                customer.getPhone(),
                customer.getPhone()
        );
    }

    public static Order toOrder(OrderDto orderDto) {
        return new Order(
                orderDto.getOrder_id(),
                orderDto.getBooks(),
                orderDto.getCustomerId(),
                orderDto.getCreatedDate(),
                orderDto.getSum()
        );
    }

    public static OrderDto toOrderDto(Order order) {
        return new OrderDto(
                order.getOrder_id(),
                order.getBooks(),
                order.getCustomerId(),
                order.getCreatedDate(),
                order.getSum()
        );
    }

}
