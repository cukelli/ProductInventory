package product.inventory.service;

import dto.product.ProductRequestBody;
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

    public List<ProductEntity> getPagedProducts(int pageIndex, int pageSize, String sortBy, String order) {
        return productRepository.findProductsWithPagination(pageIndex, pageSize, sortBy, order);
    }

    public long getTotalProductsCount() {
        return productRepository.count();
    }

    public List<ProductEntity> getAllProductsSorted(String sortBy, String order) {
        return productRepository.getAllProductsSorted(sortBy, order);
    }

    public List<ProductEntity> getAllProducts() {
        return productRepository.getAllProducts();
    }

    public ProductEntity createProduct(ProductRequestBody productRequestBody) {
        ProductEntity productEntity = new ProductEntity();
        Optional<CategoryEntity> category = categoryService.getCategoryById(productRequestBody.getCategoryEntity());
        category.ifPresent(productEntity::setCategoryEntity);
        productEntity.setName(productRequestBody.getName());
        productEntity.setDescription(productRequestBody.getDescription());
        productEntity.setPrice(productRequestBody.getPrice());
        productEntity.setQuantity(productRequestBody.getQuantity());
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

    public Optional<ProductEntity> updateProduct(UUID productId, ProductRequestBody updateProductBody) {
        Optional<ProductEntity> existingProductOpt = productRepository.findProductEntityById(productId);

        if (existingProductOpt.isEmpty()) {
            return Optional.empty();
        }
        ProductEntity existingProduct = existingProductOpt.get();

        if (updateProductBody.getName() != null && !updateProductBody.getName().isEmpty())
            existingProduct.setName(updateProductBody.getName());

        if (updateProductBody.getDescription() != null && !updateProductBody.getDescription().isEmpty())
            existingProduct.setDescription(updateProductBody.getDescription());

        if (updateProductBody.getPrice() != null && updateProductBody.getPrice() > 0)
            existingProduct.setPrice(updateProductBody.getPrice());

        if (updateProductBody.getQuantity() != null && updateProductBody.getQuantity() > 0)
            existingProduct.setQuantity(updateProductBody.getQuantity());

        if (updateProductBody.getCategoryEntity() != null) {
            Optional<CategoryEntity> category = categoryService.getCategoryById(updateProductBody.getCategoryEntity());
            existingProduct.setCategoryEntity(category.get());
        }
        try {
            productRepository.updateProduct(existingProduct);
            return Optional.of(existingProduct);
        } catch (jakarta.persistence.OptimisticLockException e) {
            throw new RuntimeException("Conflict: Another update occurred on this product. Please refresh and try again.", e);
        }
    }

}
