package mate.academy.bookstoreprod.service;

import java.util.List;
import mate.academy.bookstoreprod.dto.BookDto;
import mate.academy.bookstoreprod.dto.BookSearchParametersDto;
import mate.academy.bookstoreprod.dto.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto save(CreateBookRequestDto book);

    BookDto updateById(Long id, CreateBookRequestDto book);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto searchParameters);
}
