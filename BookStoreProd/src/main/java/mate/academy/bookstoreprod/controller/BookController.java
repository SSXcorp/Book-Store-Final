package mate.academy.bookstoreprod.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreprod.dto.BookDto;
import mate.academy.bookstoreprod.dto.CreateBookRequestDto;
import mate.academy.bookstoreprod.dto.UpdateBookRequestDto;
import mate.academy.bookstoreprod.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public List<BookDto> getAll() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto save(@RequestBody CreateBookRequestDto book) {
        return bookService.save(book);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody UpdateBookRequestDto book) {
        bookService.updateById(id, book);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        bookService.deleteById(id);
        return "Book deleted successfully";
    }
}
