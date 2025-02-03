package dto;

public class CreateCategoryRequestBody {

    public String name;


    public CreateCategoryRequestBody(String name) {
        this.name = name;
    }

    public CreateCategoryRequestBody() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
