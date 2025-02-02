package product.inventory.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import java.util.List;

public class CategoryEntity extends PanacheEntityBase {

    public String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    public List<ProductEntity> products;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }
    @Override
    public String toString() {
        return "CategoryEntity{" +
                "name='" + name + '\'' +
                ", products=" + products +
                '}';
    }
}
