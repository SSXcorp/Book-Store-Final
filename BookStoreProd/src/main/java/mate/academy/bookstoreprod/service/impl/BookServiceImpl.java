package mate.academy.bookstoreprod.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreprod.dto.BookDto;
import mate.academy.bookstoreprod.dto.CreateBookRequestDto;
import mate.academy.bookstoreprod.dto.UpdateBookRequestDto;
import mate.academy.bookstoreprod.exception.EntityNotFoundException;
import mate.academy.bookstoreprod.mapper.BookMapper;
import mate.academy.bookstoreprod.model.Book;
import mate.academy.bookstoreprod.repository.BookRepository;
import mate.academy.bookstoreprod.service.BookService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

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
    public void updateById(Long id, UpdateBookRequestDto dto) {
        Book book = bookMapper.toBook(dto);
        bookRepository.updateById(id, book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
