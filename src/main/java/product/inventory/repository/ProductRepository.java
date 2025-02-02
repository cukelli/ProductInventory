package product.inventory.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import product.inventory.model.ProductEntity;

import java.util.Optional;
import java.util.UUID;

public class ProductRepository implements PanacheRepository<ProductEntity> {

    public Optional<ProductEntity> findProductEntityById(UUID id) {
        return find("id", id).firstResultOptional();
    }

}
