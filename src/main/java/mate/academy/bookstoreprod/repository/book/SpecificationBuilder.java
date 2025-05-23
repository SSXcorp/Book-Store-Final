package mate.academy.bookstoreprod.repository.book;

import mate.academy.bookstoreprod.dto.book.BookSearchParametersDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParametersDto parameters);
}
