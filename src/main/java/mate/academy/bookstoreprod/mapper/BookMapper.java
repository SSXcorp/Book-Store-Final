package mate.academy.bookstoreprod.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.bookstoreprod.config.MapperConfig;
import mate.academy.bookstoreprod.dto.book.BookDto;
import mate.academy.bookstoreprod.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstoreprod.dto.book.CreateBookRequestDto;
import mate.academy.bookstoreprod.exception.EntityNotFoundException;
import mate.academy.bookstoreprod.model.Book;
import mate.academy.bookstoreprod.model.Category;
import mate.academy.bookstoreprod.repository.category.CategoryRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
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

    @Mapping(target = "categories", ignore = true)
    void toBookWithCategories(@MappingTarget Book book,
                              CreateBookRequestDto createBookRequestDto,
                              @Context CategoryRepository categoryRepository);

    @AfterMapping
    default void setCategories(@MappingTarget Book book,
                               CreateBookRequestDto createBookRequestDto,
                               @Context CategoryRepository categoryRepository) {
        Set<Category> categories = createBookRequestDto.getCategories().stream()
                .map(id -> categoryRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Category not found with id " + id)))
                .collect(Collectors.toSet());
        book.setCategories(categories);
    }

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto dto, Book book) {
        Set<Long> ids = book.getCategories().stream()
                .filter(c -> !c.isDeleted())
                .map(Category::getId)
                .collect(Collectors.toSet());
        dto.setCategories(ids);
    }
}
