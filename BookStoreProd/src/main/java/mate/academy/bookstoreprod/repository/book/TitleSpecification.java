package mate.academy.bookstoreprod.repository.book;

import java.util.Arrays;
import mate.academy.bookstoreprod.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecification implements SpecificationProvider<Book> {
    private static final String TITLE = "title";

    @Override
    public String getKey() {
        return TITLE;
    }

    @Override
    public Specification<Book> getSpecification(String[] titles) {
        return (root, query, criteriaBuilder) -> root.get(TITLE)
                .in(Arrays.stream(titles).toArray());
    }
}
