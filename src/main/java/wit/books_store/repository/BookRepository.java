package wit.books_store.repository;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import wit.books_store.exceptions.InternalException;
import wit.books_store.exceptions.NotFoundException;
import wit.books_store.models.Book;

import java.util.List;

@Repository
@AllArgsConstructor
public class BookRepository  {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Book> bookMapper = (rs, rowNum) ->
         new Book(
                rs.getLong("book_id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getInt("price"),
                rs.getBoolean("isPresent")
        );

    public List<Book> findAll() {
        String sql = "SELECT * from books";
        return jdbcTemplate.query(sql, bookMapper);
    }

    public Book findById(Long id) {
        String sql = "SELECT * from books WHERE book_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, bookMapper, id);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException("book was not found");
        } catch (Exception ex) {
            throw new InternalException("an error occurred");
        }
    }

    public void save(Book book) {
        String sql = "INSERT INTO books (title, author, price, isPresent)  VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(), book.getPrice(), book.isPresent());
    }
}
