package product.inventory.service;

import dto.CreateCategoryRequestBody;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import product.inventory.model.CategoryEntity;
import product.inventory.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@ApplicationScoped
public class CategoryService {

    @Inject
    CategoryRepository categoryRepository;

    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.getAllCategories();
    }

    public CategoryEntity createCategory(CreateCategoryRequestBody createCategoryRequestBody) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(createCategoryRequestBody.getName());
         categoryRepository.persistAndFlush(categoryEntity);
        return categoryEntity;
    }

    public Optional<CategoryEntity> getCategoryById(UUID id) {
        return categoryRepository.findCategoryEntityById(id);

    }
}
