package validation;

import java.util.ArrayList;
import java.util.List;

public class RequestValidator {

    public static List<String> validateGetAllProductsRequest(Integer page, Integer size, String sortBy, String order) {
        List<String> errorMessages = new ArrayList<>();

        if (page != null && size != null) {
            if (page < 0 || size < 0) {
                errorMessages.add("Page and size cannot be negative numbers");
            }
        }

        if (order != null) {
            if (!order.equalsIgnoreCase("asc") && !order.equalsIgnoreCase("desc")) {
                errorMessages.add("Order can only be 'asc' or 'desc'.");
            }
        }

        if (order != null && sortBy == null) {
            errorMessages.add("Sorting order ('asc' or 'desc') can only be used when 'sortBy' is specified.");
        }

        if (sortBy != null) {
            if (!sortBy.equalsIgnoreCase("name") && !sortBy.equalsIgnoreCase("description")
                    && !sortBy.equalsIgnoreCase("price") && !sortBy.equalsIgnoreCase("quantity")) {
                errorMessages.add("Invalid parameter for sorting - it can be 'name', 'description', 'price', or 'quantity'.");
            }
        }

        return errorMessages;
    }
}

