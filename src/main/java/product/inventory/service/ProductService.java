package product.inventory.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import product.inventory.model.ProductEntity;
import product.inventory.repository.ProductRepository;
import java.util.List;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository productRepository;

    public List<ProductEntity> getPagedProducts(int pageIndex, int pageSize) {
        return productRepository.findProductsWithPagination(pageIndex, pageSize);
    }

    public long getTotalProductsCount() {
        return productRepository.count();
    }
}
