package mate.academy.bookstoreprod.service.book;

import mate.academy.bookstoreprod.dto.book.BookDto;
import mate.academy.bookstoreprod.dto.book.BookSearchParametersDto;
import mate.academy.bookstoreprod.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

    Page<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto save(CreateBookRequestDto book);

    BookDto updateById(Long id, CreateBookRequestDto book);

    void deleteById(Long id);

    Page<BookDto> search(BookSearchParametersDto searchParameters, Pageable pageable);
}
