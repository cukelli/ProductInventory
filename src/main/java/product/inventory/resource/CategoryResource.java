package product.inventory.resource;


import dto.category.CreateCategoryRequestBody;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import product.inventory.model.CategoryEntity;
import product.inventory.service.CategoryService;
import java.util.List;

@Path("/category")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {

    @Inject
    CategoryService categoryService;

    @POST
    public Response createCategory(CreateCategoryRequestBody createCategoryRequestBody) {
        CategoryEntity createdCategory = categoryService.createCategory(createCategoryRequestBody);
        return Response.ok(createdCategory).build();
    }

    @GET
    public Response getAllCategories() {
        List<CategoryEntity> allCategories = categoryService.getAllCategories();
        return Response.ok(allCategories).build();
    }
}
