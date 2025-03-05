package wit.books_store.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wit.books_store.models.Book;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class BookRepository {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    private final RowMapper<Book> bookMapper = (rs, rowNum) ->
            Book.builder()
                    .book_id(rs.getLong("book_id"))
                    .title(rs.getString("title"))
                    .author(rs.getString("author"))
                    .price(rs.getInt("price"))
                    .isPresent(rs.getBoolean("isPresent"))
                    .build();


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

    public Book save(Book book) {
        String sql = "INSERT INTO books (title, author, price, isPresent)  VALUES (:title, :author, :price, :isPresent)";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("author", book.getAuthor())
                .addValue("price", book.getPrice())
                .addValue("isPresent", book.isPresent());

        KeyHolder keyHolder = new GeneratedKeyHolder();

         namedJdbcTemplate.update(sql, parameters, keyHolder, new String[] {"book_id"});
         book.setBook_id(Objects.requireNonNull(keyHolder.getKey()).longValue());
         return book;
    }

    public List<Book> findBooksByDate(OffsetDateTime startOfDay, OffsetDateTime endOfDay) {
        String sql = "SELECT book_id, title, author, price, createdDate, isPresent FROM " +
                "books b LEFT JOIN orders o ON b.book_id = ANY (o.books) " +
                "WHERE createdDate >= :startOfDay AND createdDate <= :endOfDay";
        return namedJdbcTemplate.query(sql, Map.of("startOfDay", startOfDay, "endOfDay", endOfDay), bookMapper);
    }
}
