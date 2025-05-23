package mate.academy.bookstoreprod.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.bookstoreprod.dto.book.BookDto;
import mate.academy.bookstoreprod.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstoreprod.dto.book.BookSearchParametersDto;
import mate.academy.bookstoreprod.dto.book.CreateBookRequestDto;
import mate.academy.bookstoreprod.exception.EntityAlreadyExistsException;
import mate.academy.bookstoreprod.exception.EntityNotFoundException;
import mate.academy.bookstoreprod.mapper.BookMapper;
import mate.academy.bookstoreprod.model.Book;
import mate.academy.bookstoreprod.model.Category;
import mate.academy.bookstoreprod.repository.book.BookRepository;
import mate.academy.bookstoreprod.repository.book.BookSpecificationBuilder;
import mate.academy.bookstoreprod.service.book.BookServiceImpl;
import mate.academy.bookstoreprod.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private static final String ISBN = "1234567890";
    private static final String JAVA = "Java";
    private static final String NATURE = "Nature";
    private static final Long NONEXISTING_ID = 9999L;
    private static final Long ONE = 1L;
    private static final String SPRING = "Spring";
    private static final int TEN = 10;
    private static final String TITLE = "title";
    private static final Long TWO = 2L;
    private static final int ZERO = 0;

    private final TestUtil testUtil = new TestUtil();

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    @Test
    @DisplayName("""
            Verify find all existing BookDto return pageable
            """)
    public void findAll_Valid_ReturnsAllBookDtoPageable() {
        Pageable pageable = PageRequest.of(ZERO, TEN);
        List<Book> books = List.of(new Book());
        Page<Book> expected = new PageImpl<>(books);

        when(bookRepository.findAll(pageable)).thenReturn(expected);

        Page<BookDto> actual = bookServiceImpl.findAll(pageable);

        assertNotNull(actual);
        assertEquals(expected.getTotalElements(), actual.getTotalElements());
        verify(bookRepository).findAll(pageable);
    }

    @Test
    @DisplayName("""
            Verify BookDto returns by given valid id
            """)
    public void findById_WithValidId_ReturnsBookDto() {
        Book book = testUtil.getBook();

        BookDto bookDto = testUtil.getBookDto();

        when(bookRepository.findById(ONE)).thenReturn(Optional.of(book));
        when(bookMapper.toBookDto(any(Book.class))).thenReturn(bookDto);

        BookDto actual = bookServiceImpl.findById(ONE);

        assertNotNull(actual);
        assertEquals(ONE, actual.getId());
        verify(bookRepository).findById(ONE);
    }

    @Test
    @DisplayName("""
            Throws EntityNotFoundException when find user by invalid id
            """)
    public void findById_WithInvalidId_ThrowsEntityNotFoundException() {
        when(bookRepository.findById(NONEXISTING_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookServiceImpl.findById(NONEXISTING_ID));
        verify(bookRepository).findById(NONEXISTING_ID);
    }

    @Test
    @DisplayName("""
            Verify Book saves, BookDto returns with given valid input
            """)
    public void save_WithValidInput_ReturnsBookDto() {
        CreateBookRequestDto dto = testUtil.getCreateBookRequestDto();

        Book saved = testUtil.getBook();

        BookDto savedDto = testUtil.getBookDto();

        when(bookRepository.existsByIsbn(dto.getIsbn())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(saved);
        when(bookMapper.toBookDto(any(Book.class))).thenReturn(savedDto);
        BookDto result = bookServiceImpl.save(dto);

        assertNotNull(result);
        assertEquals(ONE, result.getId());
        assertEquals(dto.getTitle(), result.getTitle());
        assertEquals(dto.getAuthor(), result.getAuthor());
        assertEquals(dto.getPrice(), result.getPrice());
        assertEquals(dto.getCategories(), result.getCategories());
        verify(bookRepository).existsByIsbn(dto.getIsbn());
    }

    @Test
    @DisplayName("""
            Throws EntityExistsException when trying to save book with duplicate ISBN
            """)
    public void save_WithDuplicateIsbn_ThrowsEntityAlreadyExistsException() {
        CreateBookRequestDto dto = testUtil.getCreateBookRequestDto();

        when(bookRepository.existsByIsbn(ISBN)).thenReturn(true);

        assertThrows(EntityAlreadyExistsException.class, () -> bookServiceImpl.save(dto));
        verify(bookRepository).existsByIsbn(ISBN);
    }

    @Test
    @DisplayName("""
            Verify updated BookDto returns by valid input
            """)
    public void updateById_WithValidInput_ReturnsBookDto() {
        CreateBookRequestDto toUpdateDto = testUtil.getUpdateCreateBookRequestDto();
        Book updated = testUtil.getUpdatedBook();
        BookDto updatedDto = testUtil.getUpdatedBookDto();
        Book existing = testUtil.getBook();

        when(bookRepository.findById(ONE)).thenReturn(Optional.of(existing));
        when(bookRepository.save(any(Book.class))).thenReturn(updated);
        when(bookMapper.toBookDto(any(Book.class))).thenReturn(updatedDto);
        BookDto result = bookServiceImpl.updateById(ONE, toUpdateDto);

        assertNotNull(result);
        assertEquals(ONE, result.getId());
        assertNotEquals(existing.getTitle(), result.getTitle());
        assertNotEquals(existing.getAuthor(), result.getAuthor());
        assertNotEquals(existing.getPrice(), result.getPrice());
        verify(bookRepository).findById(ONE);
    }

    @Test
    @DisplayName("""
            Throws EntityNotFoundException when updating book by invalid id
            """)
    public void updateById_WithInvalidId_ThrowsEntityNotFoundException() {
        Long invalidId = NONEXISTING_ID;
        CreateBookRequestDto dto = testUtil.getCreateBookRequestDto();

        when(bookRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> bookServiceImpl.updateById(invalidId, dto));
    }

    @Test
    @DisplayName("""
            Verify search returns all BookDto by valid titles array
            """)
    public void search_WithValidTitlesArray_ReturnsAllBookDtoPageableWithGivenTitles() {
        BookSearchParametersDto searchParams = new BookSearchParametersDto();
        searchParams.setTitle(new String[]{JAVA, SPRING});

        Book javaBook = new Book();
        javaBook.setTitle(JAVA);
        Book springBook = new Book();
        springBook.setTitle(SPRING);
        Book natureBook = new Book();
        natureBook.setTitle(NATURE);

        BookDto bookDto1 = new BookDto();
        bookDto1.setTitle(JAVA);
        BookDto bookDto2 = new BookDto();
        bookDto2.setTitle(SPRING);

        Pageable pageable = PageRequest.of(ZERO, TEN);
        Page<Book> books = new PageImpl<>(List.of(javaBook, springBook));

        Specification<Book> titleSpecification =
                Specification.where((root,
                                     query,
                                     criteriaBuilder) -> root.get(TITLE)
                        .in((Object) searchParams.getTitle()));

        when(bookRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(books);
        when(bookSpecificationBuilder.build(any())).thenReturn(titleSpecification);
        when(bookMapper.toBookDto(javaBook)).thenReturn(bookDto1);
        when(bookMapper.toBookDto(springBook)).thenReturn(bookDto2);

        Page<BookDto> result = bookServiceImpl.search(searchParams, pageable);

        assertNotNull(result);
        assertEquals(TWO, result.getTotalElements());
        assertEquals(bookDto1.getTitle(), result.getContent().get(ZERO).getTitle());
        assertEquals(bookDto2.getTitle(), result.getContent().get(ONE.intValue()).getTitle());
    }

    @Test
    @DisplayName("""
            Verify findAllByCategoryId returns all BookDtoWithoutCategoryIds pageable
            """)
    public void findAllByCategoryId_WithValidId_ReturnsAllBookDtoWithoutCategoryIdsPageable() {
        Pageable pageable = PageRequest.of(ZERO, TEN);
        Category category = testUtil.getCategoryWithIdOne();

        Book bookOne = testUtil.getBook();
        bookOne.setCategories(Set.of(category));
        Book bookTwo = testUtil.getBook();
        bookTwo.setCategories(Set.of(category));

        Page<Book> books = new PageImpl<>(List.of(bookOne, bookTwo));

        when(bookRepository.findAllByCategories_id(ONE, pageable)).thenReturn(books);

        Page<BookDtoWithoutCategoryIds> result = bookServiceImpl.findAllByCategoryId(ONE, pageable);

        assertNotNull(result);
        assertEquals(result.getTotalElements(), TWO);
        verify(bookRepository).findAllByCategories_id(ONE, pageable);
    }
}
