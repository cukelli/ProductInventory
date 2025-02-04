package product.inventory;

import dto.product.ProductRequestBody;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import product.inventory.model.CategoryEntity;
import product.inventory.model.ProductEntity;
import product.inventory.resource.ProductResource;
import product.inventory.service.CategoryService;
import product.inventory.service.ProductService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
public class ProductResourceTest {

    @Mock
    ProductService productService;

    @Mock
    CategoryService categoryService;

    @InjectMocks
    ProductResource productResource;

    private ProductRequestBody validRequestBody;
    private ProductRequestBody invalidRequestBody;
    private UUID validId;
    private UUID invalidId;
    private CategoryEntity mockCategory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validId = UUID.randomUUID();
        invalidId = UUID.randomUUID();
        validRequestBody = new ProductRequestBody("Product Name", "Description", 10, 100.00, validId);
        invalidRequestBody = new ProductRequestBody("Product Name", "Description", 10, 100.00, invalidId);
        ;
        mockCategory = new CategoryEntity(validId, "Category", null);
        ProductEntity validProduct = new ProductEntity(validId, "Product Name", "Description", 100.0, 10, mockCategory);
        when(productService.createProduct(validRequestBody)).thenReturn(validProduct);
        when(productService.updateProduct(validId, validRequestBody)).thenReturn(Optional.of(validProduct));
        when(categoryService.getCategoryById(validId)).thenReturn(Optional.of(mockCategory));
        when(categoryService.getCategoryById(invalidId)).thenReturn(Optional.empty());
        when(productService.getProductById(validId)).thenReturn(Optional.of(validProduct));
        when(productService.getProductById(invalidId)).thenReturn(Optional.empty());
    }

    @Test
    @DisplayName("Get all products")
    void testGetAllProducts() {
        List<ProductEntity> mockProducts = List.of(new ProductEntity(), new ProductEntity());
        when(productService.getPagedProducts(1, 5, "name", "asc", "", 0.0, null)).thenReturn(mockProducts);
        when(productService.getTotalProductsCount()).thenReturn(10L);
        Response response = productResource.getProducts(1, 5, "name", "asc", "", 0.0, null);
        assertEquals(200, response.getStatus());
        assertTrue(response.hasEntity());
        Map<String, Object> responseMap = (Map<String, Object>) response.getEntity();
        assertTrue(responseMap.containsKey("data"));
        List<ProductEntity> responseList = (List<ProductEntity>) responseMap.get("data");
        assertEquals(2, responseList.size());
        assertEquals(10L, responseMap.get("totalItems"));
        verify(productService).getPagedProducts(1, 5, "name", "asc", "", 0.0, null);
        verify(productService).getTotalProductsCount();
    }

    @Test
    @DisplayName("Get no products")
    void testGetNoProducts() {
        List<ProductEntity> mockProducts = Collections.emptyList();
        when(productService.getPagedProducts(1, 5, "name", "asc", null, 0.0, null)).thenReturn(mockProducts);
        when(productService.getTotalProductsCount()).thenReturn(0L);
        Response response = productResource.getProducts(1, 5, "name", "asc", null, 0.0, null);
        assertEquals(200, response.getStatus());
        assertTrue(response.hasEntity());
        Map<String, Object> responseMap = (Map<String, Object>) response.getEntity();
        assertTrue(responseMap.containsKey("data"));
        List<ProductEntity> responseList = (List<ProductEntity>) responseMap.get("data");
        assertTrue(responseList.isEmpty());
        assertEquals(0L, responseMap.get("totalItems"));
        verify(productService).getPagedProducts(1, 5, "name", "asc", null, 0.0, null);
        verify(productService).getTotalProductsCount();
    }

    @Test
    @DisplayName("Successful deleted product")
    void testDeleteProductSuccess() {
        when(productService.deleteProduct(validId)).thenReturn(true);
        Response response = productResource.deleteProduct(validId);
        assertEquals(200, response.getStatus());
        verify(productService, times(1)).deleteProduct(validId);
    }


    @Test
    @DisplayName("Failed deletion of a product")
    void testDeleteProductNotFound() {
        when(productService.deleteProduct(invalidId)).thenReturn(false);
        Response response = productResource.deleteProduct(invalidId);
        assertEquals(404, response.getStatus());
        verify(productService, times(1)).deleteProduct(invalidId);
    }

    @Test
    @DisplayName("Get product by ID successfully")
    void testGetProductByIdSuccess() {
        Response response = productResource.getProductById(validId);
        assertEquals(200, response.getStatus());
        assertTrue(response.hasEntity());
        ProductEntity responseProduct = (ProductEntity) response.getEntity();
        assertEquals(validId, responseProduct.getId());
        verify(productService).getProductById(validId);
    }

    @Test
    @DisplayName("Get product by ID failed")
    void testGetProductByIdNotFound() {
        Response response = productResource.getProductById(invalidId);
        assertEquals(404, response.getStatus());
        verify(productService).getProductById(invalidId);
    }

    @Test
    @DisplayName("Create product successfully")
    void testCreateProductSuccess() {
        Response response = productResource.createProduct(validRequestBody);
        assertEquals(200, response.getStatus());
        ProductEntity createdProduct = (ProductEntity) response.getEntity();
        assertEquals(validRequestBody.getName(), createdProduct.getName());
        assertEquals(validRequestBody.getPrice(), createdProduct.getPrice());
        verify(productService).createProduct(validRequestBody);
    }

    @Test
    @DisplayName("Create product failed")
    void testCreateProductFailed() {
        Response response = productResource.createProduct(invalidRequestBody);
        assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("Update product failed")
    void testUpdateProductFailed() {
        Response response = productResource.updateProduct(invalidId, invalidRequestBody);
        assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("Update product created")
    void testUpdateProductCreated() {
        Response response = productResource.updateProduct(validId, validRequestBody);
        ProductEntity updatedProduct = (ProductEntity) response.getEntity();
        assertEquals(validRequestBody.getName(), updatedProduct.getName());
        assertEquals(200, response.getStatus());
    }
}