package wit.books_store.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import wit.books_store.models.Book;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class BookRepository {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    private final RowMapper<Book> bookMapper = (rs, rowNum) ->
         new Book(
                rs.getLong("book_id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getInt("price"),
                rs.getBoolean("isPresent")
        );

    public List<Book> findAll(Pageable pageable) {
        String sql = "SELECT * from books LIMIT :limit OFFSET :offset";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("limit", pageable.getPageSize())
                .addValue("offset", pageable.getOffset());

        return namedJdbcTemplate.query(sql, parameters, bookMapper);
    }

    public Optional<Book> findById(long id) {
        String sql = "SELECT * from books WHERE book_id = :id";
        return namedJdbcTemplate.query(sql, Map.of("id", id), bookMapper).stream().findFirst();
    }

    public List<Book> findBooksByIds(List<Long> ids) {
        String sql = "SELECT * FROM books WHERE book_id = ANY (:ids)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids.toArray(new Long[0]), java.sql.Types.ARRAY);
        return namedJdbcTemplate.query(sql, parameters, bookMapper);
    }

    public void save(Book book) {
        String sql = "INSERT INTO books (title, author, price, isPresent)  VALUES (:title, :author, :price, :isPresent)";
        namedJdbcTemplate.update(sql,
                Map.of("title", book.getTitle(), "author", book.getAuthor(),
                        "price", book.getPrice(), "isPresent", book.isPresent()));
    }

    public List<Book> findBooksByDate(OffsetDateTime startOfDay, OffsetDateTime endOfDay) {
        String sql = "SELECT book_id, title, author, price, createdDate, isPresent FROM " +
                "books b LEFT JOIN orders o ON b.book_id = ANY (o.books) " +
                "WHERE createdDate >= :startOfDay AND createdDate <= :endOfDay";
        return namedJdbcTemplate.query(sql, Map.of("startOfDay", startOfDay, "endOfDay", endOfDay), bookMapper);
    }
}
