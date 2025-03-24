package mate.academy.bookstoreprod.repository.book;

import java.util.Arrays;
import mate.academy.bookstoreprod.model.Book;
import mate.academy.bookstoreprod.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecification implements SpecificationProvider<Book> {
    private static final String author = "author";

    @Override
    public String getKey() {
        return author;
    }

    public Specification<Book> getSpecification(String[] authors) {
        return (root, query, criteriaBuilder) -> root.get(author)
                .in(Arrays.stream(authors).toArray());
    }
}
