package wit.books_store.repository;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import wit.books_store.exceptions.InternalException;
import wit.books_store.exceptions.NotFoundException;
import wit.books_store.models.Customer;

import java.util.List;

@Repository
@AllArgsConstructor
public class CustomerRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Customer> customerMapper = (rs, rowNum) ->
            new Customer(
                    rs.getLong("customer_id"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("email"),
                    rs.getString("phone")
            );

    public List<Customer> findAll() {
        String sql = "SELECT * from customers";
        return jdbcTemplate.query(sql, customerMapper);
    }

    public Customer findById(Long id) {
        String sql = "SELECT * from customers WHERE customer_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, customerMapper, id);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException("customer does not exist");
        } catch (Exception ex) {
            throw new InternalException("an error occurred");
        }
    }

    public void save(Customer customer) {
        String sql = "INSERT INTO customers (name, surname, email, phone)  VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, customer.getName(), customer.getSurname(), customer.getEmail(), customer.getPhone());
    }

}
