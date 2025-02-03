package product.inventory.service;

import dto.CreateProductRequestBody;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import product.inventory.model.CategoryEntity;
import product.inventory.model.ProductEntity;
import product.inventory.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository productRepository;

    @Inject
    CategoryService categoryService;

    public List<ProductEntity> getPagedProducts(int pageIndex, int pageSize) {
        return productRepository.findProductsWithPagination(pageIndex, pageSize);
    }

    public long getTotalProductsCount() {
        return productRepository.count();
    }

    public List<ProductEntity> getAllProducts() {
        return productRepository.getAllProducts();
    }

    public ProductEntity createProduct(CreateProductRequestBody createProductRequestBody) {
        ProductEntity productEntity = new ProductEntity();
        Optional<CategoryEntity> category = categoryService.getCategoryById(createProductRequestBody.getCategoryEntity());
        category.ifPresent(productEntity::setCategoryEntity);
        productEntity.setName(createProductRequestBody.getName());
        productEntity.setDescription(createProductRequestBody.getDescription());
        productEntity.setPrice(createProductRequestBody.getPrice());
        productEntity.setQuantity(createProductRequestBody.getQuantity());
        productRepository.persistAndFlush(productEntity);
        return productEntity;
    }

    public Optional<ProductEntity> getProductById(UUID productId) {
        return productRepository.findProductEntityById(productId);
    }

    public boolean deleteProduct(UUID productId) {
        Optional<ProductEntity> productEntity = productRepository.findProductEntityById(productId);
        if (productEntity.isPresent()) {
            productRepository.deleteProduct(productEntity.get().getId());
            return true;
        }
        return false;
    }

}
