package douglas.events.application.controller;

import douglas.events.application.dto.ParticipantDto;
import douglas.events.application.dto.response.ParticipantResponseDTO;
import douglas.events.business.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/participant")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping("/create-participation/{eventId}")
    public ResponseEntity<ParticipantResponseDTO> createParticipation(@PathVariable Long eventId, @RequestBody ParticipantDto participantDto) {
        var participant = participantService.createParticipant(eventId, ParticipantDto.toEntity(participantDto));

        return ResponseEntity.ok().body(
                new ParticipantResponseDTO(
                        participant.getId(),
                        participant.getName(),
                        participant.getEmail(),
                        participant.getParticipantType()
                )
        );
    }
}
