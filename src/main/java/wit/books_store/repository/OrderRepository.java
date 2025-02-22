package wit.books_store.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wit.books_store.models.Order;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.*;

@Repository
@AllArgsConstructor
public class OrderRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<Order> findAll(Pageable pageable) {
        String sql = "SELECT * from orders LIMIT :limit OFFSET :offset";
        return jdbcTemplate.query(sql, Map.of("limit", pageable.getPageSize(), "offset", pageable.getOffset()), this::makeOrder);
    }

    public Optional<Order> findById(long id) {
        String sql = "SELECT * from orders WHERE order_id = :id";
        Map<String, Long> source = new HashMap<>();
        source.put("id", id);
        return jdbcTemplate.query(sql, source, this::makeOrder).stream().findFirst();
    }

    public void save(Order order) {
        String sql = "INSERT INTO orders (customer_id, createdDate, sum) VALUES (:customerId, :createdDate, :sum)";
        SqlParameterSource map = new MapSqlParameterSource()
        .addValue("customerId", order.getCustomerId())
        .addValue("createdDate", order.getCreatedDate())
        .addValue("sum", order.getSum());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, map, keyHolder, new String[] {"order_id"});
        order.setOrder_id(Objects.requireNonNull(keyHolder.getKey()).longValue());
        saveBooksForOrder(order);
    }

    public List<Order> getOrdersByCustomer(long id) {
        String sql = "SELECT * FROM orders WHERE customer_id = :id";
        return jdbcTemplate.query(sql, Map.of("id", id), this::makeOrder);
    }

    private Order makeOrder(ResultSet rs, int rowNum) throws SQLException {
        List<Long> books = getBooks(rs.getLong("order_id"));

        return Order.builder()
                .order_id(rs.getLong("order_id"))
                .books(books)
                .createdDate(rs.getObject("createdDate", OffsetDateTime.class))
                .customerId(rs.getLong("customer_id"))
                .sum(rs.getDouble("sum"))
                .build();
    }

    private List<Long> getBooks(Long orderId) {
        String sql = "SELECT book_id FROM order_books " +
                "where order_id = :orderId";

        return jdbcTemplate.queryForList(sql, Map.of("orderId", orderId), Long.class);
    }

    private void saveBooksForOrder(Order order) {
        String sql = "INSERT into order_books (order_id, book_id) VALUES (:orderId, :bookId)";
        for (Long bookId : order.getBooks()) {
            jdbcTemplate.update(sql, Map.of("orderId", order.getOrder_id(), "bookId", bookId));
        }
    }

}
