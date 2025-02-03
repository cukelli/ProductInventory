package dto;

import jakarta.persistence.Column;

import java.util.UUID;

public class CreateProductRequestBody {

    private String name;

    private String description;

    private Double price;

    private Integer quantity;

    private UUID categoryEntity;

    public CreateProductRequestBody(String name, String description, Integer quantity, Double price, UUID categoryEntity) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.categoryEntity = categoryEntity;
    }

    public CreateProductRequestBody() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public UUID getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(UUID categoryEntity) {
        this.categoryEntity = categoryEntity;
    }
}
