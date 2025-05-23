package mate.academy.bookstoreprod.util;

import java.math.BigDecimal;
import java.util.Set;
import mate.academy.bookstoreprod.dto.book.BookDto;
import mate.academy.bookstoreprod.dto.book.CreateBookRequestDto;
import mate.academy.bookstoreprod.dto.category.CategoryDto;
import mate.academy.bookstoreprod.dto.category.CreateCategoryDto;
import mate.academy.bookstoreprod.model.Book;
import mate.academy.bookstoreprod.model.Category;

public class TestUtil {

    private static final String AUTHOR = "Author";
    private static final String CATEGORY_NAME = "Action";
    private static final String UPDATED_DESCRIPTION = "Updated Description";
    private static final String ISBN = "1234567890";
    private static final Long ONE = 1L;
    private static final String TITLE = "title";
    private static final Long TWO_HUNDRED = 200L;
    private static final String UPDATE_AUTHOR = "New Author";
    private static final String UPDATE_TITLE = "NEW TITLE";

    public CreateBookRequestDto getCreateBookRequestDto() {
        CreateBookRequestDto dto = new CreateBookRequestDto();
        dto.setTitle(TITLE);
        dto.setAuthor(AUTHOR);
        dto.setIsbn(ISBN);
        dto.setPrice(BigDecimal.TEN);
        dto.setCategories(Set.of(ONE));
        return dto;
    }

    public Book getBook() {
        Book book = new Book();
        book.setId(ONE);
        book.setTitle(TITLE);
        book.setAuthor(AUTHOR);
        book.setIsbn(ISBN);
        book.setPrice(BigDecimal.TEN);
        book.setCategories(Set.of(new Category()));
        return book;
    }

    public Category getCategoryWithIdOne() {
        Category category = new Category();
        category.setId(ONE);
        category.setName(CATEGORY_NAME);
        return category;
    }

    public BookDto getBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(ONE);
        bookDto.setTitle(TITLE);
        bookDto.setAuthor(AUTHOR);
        bookDto.setPrice(BigDecimal.TEN);
        bookDto.setCategories(Set.of(ONE));
        return bookDto;
    }

    public BookDto getUpdatedBookDto() {
        BookDto updatedDto = new BookDto();
        updatedDto.setId(ONE);
        updatedDto.setTitle(UPDATE_TITLE);
        updatedDto.setAuthor(UPDATE_AUTHOR);
        updatedDto.setPrice(BigDecimal.valueOf(TWO_HUNDRED));
        updatedDto.setCategories(Set.of(ONE));
        return updatedDto;
    }

    public Book getUpdatedBook() {
        Book updated = new Book();
        updated.setId(ONE);
        updated.setTitle(UPDATE_TITLE);
        updated.setAuthor(UPDATE_AUTHOR);
        updated.setPrice(BigDecimal.valueOf(TWO_HUNDRED));
        updated.setCategories(Set.of(new Category()));
        return updated;
    }

    public CreateBookRequestDto getUpdateCreateBookRequestDto() {
        CreateBookRequestDto toUpdateDto = new CreateBookRequestDto();
        toUpdateDto.setTitle(UPDATE_TITLE);
        toUpdateDto.setAuthor(UPDATE_AUTHOR);
        toUpdateDto.setDescription(UPDATED_DESCRIPTION);
        toUpdateDto.setIsbn(ISBN);
        toUpdateDto.setPrice(BigDecimal.valueOf(TWO_HUNDRED));
        toUpdateDto.setCategories(Set.of(ONE));
        return toUpdateDto;
    }

    public Category getCategory() {
        Category category = new Category();
        category.setId(ONE);
        category.setName(CATEGORY_NAME);
        return category;
    }

    public CategoryDto getCategoryDto() {
        CategoryDto dto = new CategoryDto();
        dto.setId(ONE);
        dto.setName(CATEGORY_NAME);
        return dto;
    }

    public CreateCategoryDto getCreateCategoryDto() {
        CreateCategoryDto dto = new CreateCategoryDto();
        dto.setName(CATEGORY_NAME);
        return dto;
    }
}
