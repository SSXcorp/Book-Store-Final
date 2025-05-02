package mate.academy.bookstoreprod.service.book;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreprod.dto.book.BookDto;
import mate.academy.bookstoreprod.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstoreprod.dto.book.BookSearchParametersDto;
import mate.academy.bookstoreprod.dto.book.CreateBookRequestDto;
import mate.academy.bookstoreprod.exception.EntityAlreadyExistsException;
import mate.academy.bookstoreprod.exception.EntityNotFoundException;
import mate.academy.bookstoreprod.mapper.BookMapper;
import mate.academy.bookstoreprod.model.Book;
import mate.academy.bookstoreprod.repository.book.BookRepository;
import mate.academy.bookstoreprod.repository.book.BookSpecificationBuilder;
import mate.academy.bookstoreprod.repository.category.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;
    private final CategoryRepository categoryRepository;

    @Override
    public BookDto save(CreateBookRequestDto dto) {
        if (bookRepository.existsByIsbn(dto.getIsbn())) {
            throw new EntityAlreadyExistsException("Book with isbn " + dto.getIsbn()
                    + " already exists");
        }
        Book book = new Book();
        bookMapper.toBookWithCategories(book, dto, categoryRepository);
        return bookMapper.toBookDto(bookRepository.save(book));
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).map(bookMapper::toBookDto);
    }

    @Override
    public BookDto findById(Long id) {
        return bookMapper.toBookDto(bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id)));
    }

    @Override
    public BookDto updateById(Long id, CreateBookRequestDto dto) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book not found with id: " + id));
        bookMapper.toBookWithCategories(book, dto, categoryRepository);
        return bookMapper.toBookDto(bookRepository.save(book));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Page<BookDto> search(BookSearchParametersDto searchParameters, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification, pageable)
                .map(bookMapper::toBookDto);
    }

    @Override
    public Page<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId, Pageable pageable) {
        Page<Book> booksPage = bookRepository.findAllByCategories_id(categoryId, pageable);
        return booksPage.map(bookMapper::toDtoWithoutCategories);
    }
}
