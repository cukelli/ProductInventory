package product.inventory.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import product.inventory.model.ProductEntity;
import product.inventory.service.ProductService;
import util.ResponseUtil;
import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductService productService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts(@QueryParam("page") int page,
                                @QueryParam("size") int size) {
        List<ProductEntity> products = productService.getPagedProducts(page, size);
        long totalItems = productService.getTotalProductsCount();
        return ResponseUtil.createPaginatedResponse(products, totalItems, page, size);
    }
}
