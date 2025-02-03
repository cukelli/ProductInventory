package product.inventory.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import product.inventory.model.CategoryEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CategoryRepository implements PanacheRepository<CategoryEntity> {

    public Optional<CategoryEntity> findCategoryEntityById(UUID id) {
        return find("id", id).firstResultOptional();
    }

    public List<CategoryEntity> getAllCategories() {
        return listAll();
    }
}
