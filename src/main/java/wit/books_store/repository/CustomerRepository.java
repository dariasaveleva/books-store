package wit.books_store.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import wit.books_store.models.Customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CustomerRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<Customer> customerMapper = (rs, rowNum) ->
            new Customer(
                    rs.getLong("customer_id"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("email"),
                    rs.getString("phone")
            );

    public List<Customer> findAll(Pageable pageable) {
        String sql = "SELECT * from customers LIMIT :limit OFFSET :offset";
        return jdbcTemplate.query(sql, Map.of("limit", pageable.getPageSize(), "offset", pageable.getOffset()),
                customerMapper);
    }

    public Optional<Customer> findById(long id) {
        String sql = "SELECT * from customers WHERE customer_id = :id";
        Map<String, Long> source = new HashMap<>();
        source.put("id", id);
        return jdbcTemplate.query(sql, source, customerMapper).stream().findFirst();

    }

    public Optional<Customer> findByEmailOrPhone(String email, String phone) {
        String sql = "SELECT * from customers WHERE email = :email OR phone = :phone";
        Map<String, String> source = new HashMap<>();
        source.put("email", email);
        source.put("phone", phone);
        return jdbcTemplate.query(sql, source, customerMapper).stream().findFirst();
    }

    public void save(Customer customer) {
        String sql = "INSERT INTO customers (name, surname, email, phone)  VALUES (:name, :surname, :email, :phone)";
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("name", customer.getName())
                .addValue("surname", customer.getSurname())
                .addValue("email", customer.getEmail())
                .addValue("phone", customer.getPhone());

        jdbcTemplate.update(sql, source);
    }

}
