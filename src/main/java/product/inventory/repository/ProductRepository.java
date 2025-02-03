package product.inventory.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
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

    public List<ProductEntity> findProductsWithPagination(int pageIndex, int pageSize) {
        return findAll().page(Page.of(pageIndex, pageSize)).list();
    }

    public List<ProductEntity> getAllProducts() {
        return listAll();
    }

    public boolean deleteProduct(UUID productId) {
        return delete("id = ?1", productId) > 0;
    }

}
