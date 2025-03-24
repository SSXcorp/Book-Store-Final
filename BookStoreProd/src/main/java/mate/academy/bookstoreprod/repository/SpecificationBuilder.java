package mate.academy.bookstoreprod.repository;

import mate.academy.bookstoreprod.dto.BookSearchParametersDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParametersDto parameters);
}
