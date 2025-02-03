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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
public class ProductResourceTest {

    @Mock
    ProductService productService;

    @InjectMocks
    ProductResource productResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Get all products successfully (Products found)")
    void testGetAllProducts() {
        List<ProductEntity> mockProducts = List.of(new ProductEntity(), new ProductEntity());
        when(productService.getPagedProducts(1, 5)).thenReturn(mockProducts);
        when(productService.getTotalProductsCount()).thenReturn(10L);
        Response response = productResource.getProducts(1, 5);
        assertEquals(200, response.getStatus());
        assertTrue(response.hasEntity());
        Map<String, Object> responseMap = (Map<String, Object>) response.getEntity();
        assertTrue(responseMap.containsKey("data"));
        List<ProductEntity> responseList = (List<ProductEntity>) responseMap.get("data");
        assertEquals(2, responseList.size());
        assertEquals(10L, responseMap.get("totalItems"));
        assertEquals(1, responseMap.get("currentPage"));
        assertEquals(5, responseMap.get("pageSize"));
        assertEquals(2, responseMap.get("totalPages"));
        verify(productService).getPagedProducts(1, 5);
        verify(productService).getTotalProductsCount();
    }

    @Test
    @DisplayName("Get all products successfully (No products found)")
    void testGetNoProducts() {
        List<ProductEntity> mockProducts = Collections.emptyList();
        when(productService.getPagedProducts(1, 5)).thenReturn(mockProducts);
        when(productService.getTotalProductsCount()).thenReturn(0L);
        Response response = productResource.getProducts(1, 5);
        assertEquals(200, response.getStatus());
        assertTrue(response.hasEntity());
        Map<String, Object> responseMap = (Map<String, Object>) response.getEntity();
        assertTrue(responseMap.containsKey("data"));
        List<ProductEntity> responseList = (List<ProductEntity>) responseMap.get("data");
        assertTrue(responseList.isEmpty());
        assertEquals(0L, responseMap.get("totalItems"));
        assertEquals(1, responseMap.get("currentPage"));
        assertEquals(5, responseMap.get("pageSize"));
        assertEquals(0, responseMap.get("totalPages"));
        verify(productService).getPagedProducts(1, 5);
        verify(productService).getTotalProductsCount();
    }
}
