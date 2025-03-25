package mate.academy.bookstoreprod.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreprod.dto.BookDto;
import mate.academy.bookstoreprod.dto.BookSearchParametersDto;
import mate.academy.bookstoreprod.dto.CreateBookRequestDto;
import mate.academy.bookstoreprod.exception.EntityNotFoundException;
import mate.academy.bookstoreprod.mapper.BookMapper;
import mate.academy.bookstoreprod.model.Book;
import mate.academy.bookstoreprod.repository.BookRepository;
import mate.academy.bookstoreprod.repository.book.BookSpecificationBuilder;
import mate.academy.bookstoreprod.service.BookService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto dto) {
        Book book = bookMapper.toBook(dto);
        return bookMapper.toBookDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toBookDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        return bookMapper.toBookDto(bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id)));
    }

    @Override
    public BookDto updateById(Long id, CreateBookRequestDto dto) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not found with id: " + id);
        }
        Book book = bookMapper.toBook(dto);
        book.setId(id);
        bookRepository.save(book);
        return bookMapper.toBookDto(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto searchParameters) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification)
                .stream().map(bookMapper::toBookDto)
                .toList();
    }
}
