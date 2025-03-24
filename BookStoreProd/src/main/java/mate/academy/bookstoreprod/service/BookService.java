package mate.academy.bookstoreprod.service;

import java.util.List;
import mate.academy.bookstoreprod.dto.BookDto;
import mate.academy.bookstoreprod.dto.CreateBookRequestDto;

public interface BookService {

    List<BookDto> findAll();

    BookDto findById(Long id);

    BookDto save(CreateBookRequestDto book);

    BookDto updateById(Long id, CreateBookRequestDto book);

    void deleteById(Long id);
}
