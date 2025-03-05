package wit.books_store.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import wit.books_store.dto.BookDto;
import wit.books_store.models.Book;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {

    Book toBook(BookDto bookDto);
    BookDto toBookDto(Book book);
}
