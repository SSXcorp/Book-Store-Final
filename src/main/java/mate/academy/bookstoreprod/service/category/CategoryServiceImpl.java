package mate.academy.bookstoreprod.service.category;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreprod.dto.category.CategoryDto;
import mate.academy.bookstoreprod.dto.category.CreateCategoryDto;
import mate.academy.bookstoreprod.exception.EntityAlreadyExistsException;
import mate.academy.bookstoreprod.exception.EntityNotFoundException;
import mate.academy.bookstoreprod.mapper.CategoryMapper;
import mate.academy.bookstoreprod.model.Category;
import mate.academy.bookstoreprod.repository.category.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(mapper::toCategoryDto);
    }

    @Override
    public CategoryDto getById(Long id) {
        return mapper.toCategoryDto(categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Category not found with id: " + id)
        ));
    }

    @Override
    public CategoryDto save(CreateCategoryDto dto) {
        if (categoryRepository.existsByName(dto.getName())) {
            throw new EntityAlreadyExistsException("Category with name " + dto.getName()
                    + " already exists");
        }
        return mapper.toCategoryDto(categoryRepository.save(mapper.toCategory(dto)));
    }

    @Override
    public CategoryDto update(Long id, CreateCategoryDto dto) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Category not found with id: " + id));
        mapper.updateCategoryFromDto(dto, category);
        return mapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
