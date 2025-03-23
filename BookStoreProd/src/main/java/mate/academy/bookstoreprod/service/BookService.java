package mate.academy.bookstoreprod.service;

import java.util.List;
import mate.academy.bookstoreprod.dto.BookDto;
import mate.academy.bookstoreprod.dto.CreateBookRequestDto;
import mate.academy.bookstoreprod.dto.UpdateBookRequestDto;

public interface BookService {

    List<BookDto> findAll();

    BookDto findById(Long id);

    BookDto save(CreateBookRequestDto book);

    void updateById(Long id, UpdateBookRequestDto book);

    void deleteById(Long id);
}
