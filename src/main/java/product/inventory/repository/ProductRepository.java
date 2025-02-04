package product.inventory.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import product.inventory.model.ProductEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<ProductEntity> {

    public Optional<ProductEntity> findProductEntityById(UUID id) {
        return find("id", id).firstResultOptional();
    }

    public List<ProductEntity> findProductsWithPagination(int pageIndex, int pageSize, String sortBy, String order, String name, Double minPrice, Double maxPrice) {
        Sort sort = getSort(sortBy, order);
        return find("name like ?1 and price >= ?2 and price <= ?3", sort, "%" + (name != null ? name : "") + "%",
                minPrice != null ? minPrice : 0.0, maxPrice != null ? maxPrice : Double.MAX_VALUE)
                .page(Page.of(pageIndex, pageSize))
                .list();
    }


    private Sort getSort(String sortBy, String order) {
        boolean isAscending = "asc".equalsIgnoreCase(order);
        if ("name".equalsIgnoreCase(sortBy)) {
            return isAscending ? Sort.by("name").ascending() : Sort.by("name").descending();
        } else if ("price".equalsIgnoreCase(sortBy)) {
            return isAscending ? Sort.by("price").ascending() : Sort.by("price").descending();
        } else if ("description".equalsIgnoreCase(sortBy)) {
            return isAscending ? Sort.by("description").ascending() : Sort.by("description").descending();
        } else if ("quantity".equalsIgnoreCase(sortBy)) {
            return isAscending ? Sort.by("quantity").ascending() : Sort.by("quantity").descending();
        }
        return isAscending ? Sort.by("name").ascending() : Sort.by("name").descending();
    }

    public List<ProductEntity> getAllProducts() {
        return listAll();
    }

    public boolean deleteProduct(UUID productId) {
        return delete("id = ?1", productId) > 0;
    }

    public void updateProduct(ProductEntity productEntity) {
        persistAndFlush(productEntity);
    }

}
