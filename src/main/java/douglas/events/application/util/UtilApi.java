package douglas.events.application.util;

import douglas.events.application.dto.response.ApiResponse;
import douglas.events.application.dto.response.PaginationResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UtilApi {

    public static  <E, D> ResponseEntity<ApiResponse<D>> getApiResponseEntity(
            @NotNull Page<E> page,
            @NotNull Function<E, D> mapper) {

        List<D> dtoList = page.getContent()
                .stream()
                .map(mapper)
                .collect(Collectors.toList());

        var response = new ApiResponse<>(
                dtoList,
                new PaginationResponse(
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages()
                )
        );

        return ResponseEntity.ok(response);
    }

}