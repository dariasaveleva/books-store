package wit.books_store.repository;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import wit.books_store.exceptions.InternalException;
import wit.books_store.exceptions.NotFoundException;
import wit.books_store.models.Order;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class OrderRepository {
    private final JdbcTemplate jdbcTemplate;

    public List<Order> findAll() {
        String sql = "SELECT * from orders";
        return jdbcTemplate.query(sql, this::makeOrder);
    }

    public Order findById(Long id) {
        String sql = "SELECT * from orders WHERE order_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::makeOrder, id);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException("order does not exist");
        } catch (Exception ex) {
            throw new InternalException("an error occurred");
        }
    }

    public void save(Order order) {
        String sql = "INSERT INTO Orders (customer_id, books, createddate, sum)  VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement stat = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stat.setLong(1, order.getCustomerId());
            stat.setString(2, String.join(",", order.getBooks().toString()));
            stat.setDate(3, java.sql.Date.valueOf(order.getCreatedDate()));
            stat.setDouble(4, order.getSum());
            return stat;
        });
    }

    public List<Order> getOrdersByCustomer(Long id) {
        String sql = "SELECT * FROM orders WHERE customer_id = ?";
        try {
            return jdbcTemplate.query(sql, this::makeOrder, id);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException("customer made no orders");
        } catch (Exception ex) {
            throw new InternalException("an error occurred");
        }
    }

    private Order makeOrder(ResultSet rs, int rowNum) throws SQLException {
            Order order = new Order();
            order.setOrder_id(rs.getLong("order_id"));
            order.setCreatedDate(rs.getDate("createdDate").toLocalDate());
            order.setCustomerId(rs.getLong("customer_id"));
            order.setSum(rs.getDouble("sum"));

            String books = rs.getString("books");
            if (StringUtils.isNotBlank(books)) {
                String[] booksIds = books.split(",");
                order.setBooks(Arrays.stream(booksIds)
                        .map(id -> (long) booksIds.length)
                        .collect(Collectors.toList()));
            }
            return order;
        }
    }
