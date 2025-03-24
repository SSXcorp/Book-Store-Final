package mate.academy.bookstoreprod.repository.book;

import java.util.Arrays;
import mate.academy.bookstoreprod.model.Book;
import mate.academy.bookstoreprod.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecification implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "title";
    }

    @Override
    public Specification<Book> getSpecification(String[] titles) {
        return (root, query, criteriaBuilder) -> root.get("title").in(Arrays.stream(titles).toArray());
    }
}
