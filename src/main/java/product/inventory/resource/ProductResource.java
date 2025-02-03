package product.inventory.resource;

import dto.CreateProductRequestBody;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import product.inventory.model.CategoryEntity;
import product.inventory.model.ProductEntity;
import product.inventory.service.CategoryService;
import product.inventory.service.ProductService;
import util.ResponseUtil;
import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductService productService;

    @Inject
    CategoryService categoryService;

    @GET
    public Response getProducts(@QueryParam("page") Integer page,
                                @QueryParam("size") Integer size) {
        if (page == null || size == null) {
            List<ProductEntity> allProducts = productService.getAllProducts();
            return ResponseUtil.createResponse("All products retrieved successfully", allProducts);
        }
        List<ProductEntity> products = productService.getPagedProducts(page, size);
        long totalItems = productService.getTotalProductsCount();
        return ResponseUtil.createPaginatedResponse(products, totalItems, page, size);
    }

    @POST
    public Response createProduct(CreateProductRequestBody createProductRequestBody) {
        ProductEntity createdProduct = productService.createProduct(createProductRequestBody);
        return Response.ok(createdProduct).build();
    }

}
