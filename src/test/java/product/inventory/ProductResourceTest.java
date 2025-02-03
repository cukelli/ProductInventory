package product.inventory;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import product.inventory.model.CategoryEntity;
import product.inventory.model.ProductEntity;
import product.inventory.resource.ProductResource;
import java.util.UUID;


@QuarkusTest
public class ProductResourceTest {

    @Inject
    ProductResource productResource;

    private AutoCloseable closeable;

    public static ProductEntity generateProductEntity() {
        return new ProductEntity(UUID.randomUUID(),"name", "description",
                122.23, 3, new CategoryEntity());
    }

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Get all products successfully")
    void testGetAllProducts() {
        Response response = productResource.getProducts(2, 4);
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertTrue(response.hasEntity());
    }
}
