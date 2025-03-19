package mate.academy.bookstoreprod.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreprod.model.Book;
import mate.academy.bookstoreprod.repository.BookRepository;
import mate.academy.bookstoreprod.service.BookService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
