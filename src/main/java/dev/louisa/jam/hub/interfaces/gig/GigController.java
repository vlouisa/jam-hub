package dev.louisa.jam.hub.interfaces.gig;

import dev.louisa.jam.hub.application.gig.GigApplicationService;
import dev.louisa.jam.hub.domain.band.BandId;
import dev.louisa.jam.hub.domain.gig.GigId;
import dev.louisa.jam.hub.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/bands/{bandId}/gigs")
@RequiredArgsConstructor
public class GigController {
    private final GigApplicationService gigService;

    @PostMapping
    public ResponseEntity<GigId> planGig(@PathVariable("bandId") UUID bandId, @RequestBody GigRequest request) {
        // TODO: Replace with actual user ID from authentication context
        UUID uswerUUID = UUID.fromString("07072e02-819a-4f6f-8639-d349d29c72c0");

        GigId gigId = gigService.planGigForBand(
                UserId.generate(uswerUUID),
                BandId.generate(bandId), 
                request.toDetails());
        return ResponseEntity.ok(gigId);
    }
}
