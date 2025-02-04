package product.inventory;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import product.inventory.model.ProductEntity;
import product.inventory.resource.ProductResource;
import product.inventory.service.ProductService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
public class ProductResourceTest {

    @Mock
    ProductService productService;

    @InjectMocks
    ProductResource productResource;

    private UUID validProductId;
    private UUID invalidProductId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validProductId = UUID.randomUUID();
        invalidProductId = UUID.randomUUID();
        ProductEntity validProduct = new ProductEntity();
        validProduct.setId(validProductId);
    }

    @Test
    @DisplayName("Get all products successfully (Products found)")
    void testGetAllProducts() {
        List<ProductEntity> mockProducts = List.of(new ProductEntity(), new ProductEntity());
        when(productService.getPagedProducts(1, 5, "name","asc")).thenReturn(mockProducts);
        when(productService.getTotalProductsCount()).thenReturn(10L);
        Response response = productResource.getProducts(1, 5, "name", "asc");
        assertEquals(200, response.getStatus());
        assertTrue(response.hasEntity());
        Map<String, Object> responseMap = (Map<String, Object>) response.getEntity();
        assertTrue(responseMap.containsKey("data"));
        List<ProductEntity> responseList = (List<ProductEntity>) responseMap.get("data");
        assertEquals(2, responseList.size());
        assertEquals(10L, responseMap.get("totalItems"));
        verify(productService).getPagedProducts(1, 5, "name", "asc");
        verify(productService).getTotalProductsCount();
    }

    @Test
    @DisplayName("Get all products successfully (No products found)")
    void testGetNoProducts() {
        List<ProductEntity> mockProducts = Collections.emptyList();
        when(productService.getPagedProducts(1, 5, "name", "asc")).thenReturn(mockProducts);
        when(productService.getTotalProductsCount()).thenReturn(0L);
        Response response = productResource.getProducts(1, 5, "name", "asc");
        assertEquals(200, response.getStatus());
        assertTrue(response.hasEntity());
        Map<String, Object> responseMap = (Map<String, Object>) response.getEntity();
        assertTrue(responseMap.containsKey("data"));
        List<ProductEntity> responseList = (List<ProductEntity>) responseMap.get("data");
        assertTrue(responseList.isEmpty());
        assertEquals(0L, responseMap.get("totalItems"));
        verify(productService).getPagedProducts(1, 5, "name", "asc");
        verify(productService).getTotalProductsCount();
    }

    @Test
    @DisplayName("Successful deleted product")
    void testDeleteProductSuccess() {
        when(productService.deleteProduct(validProductId)).thenReturn(true);
        Response response = productResource.deleteProduct(validProductId);
        assertEquals(200, response.getStatus());
        verify(productService, times(1)).deleteProduct(validProductId);
    }


    @Test
    @DisplayName("Failed deletion of a product")
    void testDeleteProductNotFound() {
        when(productService.deleteProduct(invalidProductId)).thenReturn(false);
        Response response = productResource.deleteProduct(invalidProductId);
        assertEquals(404, response.getStatus());
        verify(productService, times(1)).deleteProduct(invalidProductId);
    }
}