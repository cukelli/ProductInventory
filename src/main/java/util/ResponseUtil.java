package util;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseUtil {

    public static <T> Response createPaginatedResponse(List<T> data, long totalItems, int page, int size) {
        int totalPages = (int) Math.ceil((double) totalItems / size);
        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        response.put("totalItems", totalItems);
        response.put("currentPage", page);
        response.put("pageSize", size);
        response.put("totalPages", totalPages);
        return Response.ok(response).build();
    }
}
