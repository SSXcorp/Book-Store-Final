package mate.academy.bookstoreprod.mapper;

import mate.academy.bookstoreprod.config.MapperConfig;
import mate.academy.bookstoreprod.dto.book.BookDto;
import mate.academy.bookstoreprod.dto.book.CreateBookRequestDto;
import mate.academy.bookstoreprod.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toBookDto(Book book);

    Book toBook(CreateBookRequestDto createBookRequestDto);
}
