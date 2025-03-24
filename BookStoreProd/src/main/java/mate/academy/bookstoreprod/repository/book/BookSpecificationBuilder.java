package mate.academy.bookstoreprod.repository.book;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreprod.dto.BookSearchParametersDto;
import mate.academy.bookstoreprod.model.Book;
import mate.academy.bookstoreprod.repository.SpecificationBuilder;
import mate.academy.bookstoreprod.repository.SpecificationProviderManager;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto parameters) {
        Specification<Book> spec = Specification.where(null);
        if (parameters.getTitle() != null && parameters.getTitle().length > 0) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider("title").getSpecification(parameters.getTitle()));
        }
        if (parameters.getAuthor() != null && parameters.getAuthor().length > 0) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider("author").getSpecification(parameters.getAuthor()));
        }
        if (parameters.getPrice() != null && parameters.getPrice().length == 2) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider("price").getSpecification(parameters.getPrice()));
        }
        return spec;
    }
}
