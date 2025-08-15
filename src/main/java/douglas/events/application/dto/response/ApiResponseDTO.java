package douglas.events.application.dto.response;

import java.util.List;

public record ApiResponseDTO(
        String message,
        String version,
        List<Collaborator> collaborators
) {
}
