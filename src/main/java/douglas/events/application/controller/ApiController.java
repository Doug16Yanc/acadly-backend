package douglas.events.application.controller;

import douglas.events.application.dto.response.ApiResponseDTO;
import douglas.events.application.dto.response.Collaborator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class ApiController {

    @GetMapping
    public ResponseEntity<ApiResponseDTO> index() {
        return ResponseEntity.ok(
                new ApiResponseDTO(
                        "Welcome to Acadly API",
                        "beta-1.0",
                        List.of(
                                new Collaborator(
                                        "Douglas Holanda",
                                        "douglasholanda3195@gmail.com",
                                        "https://github.com/Doug16Yanc"
                                ),
                                new Collaborator(
                                        "Wesley Sousa",
                                        "wesley300rodrigues@gmail.com",
                                        "https://github.com/Wesley00s"
                                )
                        )
                )
        );
    }
}
