package product.inventory.resource;


import dto.CreateCategoryRequestBody;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import product.inventory.model.CategoryEntity;
import product.inventory.service.CategoryService;

@Path("/categories")
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

}
