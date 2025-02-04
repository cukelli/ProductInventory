package product.inventory.resource;

import dto.CreateProductRequestBody;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import product.inventory.model.ProductEntity;
import product.inventory.service.ProductService;
import util.ResponseUtil;
import validation.RequestValidator;

import javax.xml.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductService productService;


    @GET
    public Response getProducts(@QueryParam("page") Integer page,
                                @QueryParam("size") Integer size,
                                @QueryParam("sortBy") String sortBy,
                                @QueryParam("order") String order) {

        List<String> validationErrors = RequestValidator.validateGetAllProductsRequest(page, size, sortBy, order);

        if (!validationErrors.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(validationErrors).build();
        }

        if (page != null && size != null) {
            List<ProductEntity> products = productService.getPagedProducts(page, size, sortBy, order);
            long totalItems = productService.getTotalProductsCount();
            return ResponseUtil.createPaginatedResponse(products, totalItems, page, size);
        }

        List<ProductEntity> allProducts = (sortBy != null)
                ? productService.getAllProductsSorted(sortBy, order)
                : productService.getAllProducts();

        return ResponseUtil.createResponse("All products retrieved successfully", allProducts);
    }

    @POST
    public Response createProduct(CreateProductRequestBody createProductRequestBody) {
        ProductEntity createdProduct = productService.createProduct(createProductRequestBody);
        return Response.ok(createdProduct).build();
    }

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") UUID id) {
        Optional<ProductEntity> product = productService.getProductById(id);
        if (product.isPresent()) {
            return Response.ok(product.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") UUID id) {
        boolean isProductDeleted = productService.deleteProduct(id);
        if (isProductDeleted) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
