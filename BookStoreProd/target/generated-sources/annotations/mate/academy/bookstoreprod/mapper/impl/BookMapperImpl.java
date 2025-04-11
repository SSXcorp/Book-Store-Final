package mate.academy.bookstoreprod.mapper.impl;

import javax.annotation.processing.Generated;
import mate.academy.bookstoreprod.dto.book.BookDto;
import mate.academy.bookstoreprod.dto.book.CreateBookRequestDto;
import mate.academy.bookstoreprod.mapper.BookMapper;
import mate.academy.bookstoreprod.model.Book;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-11T10:59:06+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.11 (JetBrains s.r.o.)"
)
@Component
public class BookMapperImpl implements BookMapper {

    @Override
    public BookDto toBookDto(Book book) {
        if ( book == null ) {
            return null;
        }

        BookDto bookDto = new BookDto();

        if ( book.getId() != null ) {
            bookDto.setId( book.getId() );
        }
        if ( book.getTitle() != null ) {
            bookDto.setTitle( book.getTitle() );
        }
        if ( book.getAuthor() != null ) {
            bookDto.setAuthor( book.getAuthor() );
        }
        if ( book.getPrice() != null ) {
            bookDto.setPrice( book.getPrice() );
        }
        if ( book.getDescription() != null ) {
            bookDto.setDescription( book.getDescription() );
        }
        if ( book.getCoverImage() != null ) {
            bookDto.setCoverImage( book.getCoverImage() );
        }

        return bookDto;
    }

    @Override
    public Book toBook(CreateBookRequestDto createBookRequestDto) {
        if ( createBookRequestDto == null ) {
            return null;
        }

        Book book = new Book();

        if ( createBookRequestDto.getTitle() != null ) {
            book.setTitle( createBookRequestDto.getTitle() );
        }
        if ( createBookRequestDto.getAuthor() != null ) {
            book.setAuthor( createBookRequestDto.getAuthor() );
        }
        if ( createBookRequestDto.getIsbn() != null ) {
            book.setIsbn( createBookRequestDto.getIsbn() );
        }
        if ( createBookRequestDto.getPrice() != null ) {
            book.setPrice( createBookRequestDto.getPrice() );
        }
        if ( createBookRequestDto.getDescription() != null ) {
            book.setDescription( createBookRequestDto.getDescription() );
        }
        if ( createBookRequestDto.getCoverImage() != null ) {
            book.setCoverImage( createBookRequestDto.getCoverImage() );
        }

        return book;
    }
}
