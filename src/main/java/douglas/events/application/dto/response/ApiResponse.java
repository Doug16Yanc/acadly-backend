package douglas.events.application.dto.response;

import java.util.List;

public record ApiResponse<T>(
        List<T> data,
        PaginationResponse pagination
) {
}