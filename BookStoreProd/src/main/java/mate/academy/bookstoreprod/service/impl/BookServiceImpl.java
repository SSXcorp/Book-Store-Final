package mate.academy.bookstoreprod.service.impl;

import java.util.List;
import mate.academy.bookstoreprod.model.Book;
import mate.academy.bookstoreprod.repository.BookRepository;
import mate.academy.bookstoreprod.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            throw new RuntimeException("No books found");
        }
        return books;
    }
}
