package mate.academy.bookstoreprod.repository.book;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreprod.dto.book.BookSearchParametersDto;
import mate.academy.bookstoreprod.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String AUTHOR = "author";
    private static final String CATEGORY = "categories";
    private static final String PRICE = "price";
    private static final String TITLE = "title";
    private static final int TWO = 2;
    private static final int ZERO = 0;
    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto parameters) {
        Specification<Book> spec = Specification.where(null);
        if (parameters.getTitle() != null && parameters.getTitle().length > ZERO) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider(TITLE)
                    .getSpecification(parameters.getTitle()));
        }
        if (parameters.getAuthor() != null && parameters.getAuthor().length > ZERO) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider(AUTHOR)
                    .getSpecification(parameters.getAuthor()));
        }
        if (parameters.getPrice() != null && parameters.getPrice().length == TWO) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider(PRICE)
                    .getSpecification(parameters.getPrice()));
        }
        if (parameters.getCategory() != null && parameters.getCategory().length > ZERO) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider(CATEGORY)
                    .getSpecification(parameters.getCategory()));
        }
        return spec;
    }
}
