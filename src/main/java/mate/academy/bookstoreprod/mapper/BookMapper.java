package mate.academy.bookstoreprod.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.bookstoreprod.config.MapperConfig;
import mate.academy.bookstoreprod.dto.book.BookDto;
import mate.academy.bookstoreprod.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstoreprod.dto.book.CreateBookRequestDto;
import mate.academy.bookstoreprod.model.Book;
import mate.academy.bookstoreprod.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = CategoryMapper.class)
public interface BookMapper {

    @Mapping(target = "categories", ignore = true)
    BookDto toBookDto(Book book);

    @Mapping(target = "categories", ignore = true)
    Book toBook(CreateBookRequestDto createBookRequestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto dto, Book book) {
        Set<Long> ids = book.getCategories().stream()
                .filter(c -> !c.isDeleted())
                .map(Category::getId)
                .collect(Collectors.toSet());
        dto.setCategories(ids);
    }
}
