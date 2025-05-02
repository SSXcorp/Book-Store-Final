package mate.academy.bookstoreprod.service.category;

import mate.academy.bookstoreprod.dto.category.CategoryDto;
import mate.academy.bookstoreprod.dto.category.CreateCategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CreateCategoryDto categoryDto);

    CategoryDto update(Long id, CreateCategoryDto categoryDto);

    void deleteById(Long id);
}
