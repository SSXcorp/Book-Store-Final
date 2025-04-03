package mate.academy.bookstoreprod.repository.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreprod.model.Book;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {

    private final List<SpecificationProvider<Book>> specificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return specificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No specification provider found for key: "
                        + key));
    }
}
