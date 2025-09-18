package dev.louisa.jam.hub.interfaces.gig;

import dev.louisa.jam.hub.application.gig.GigApplicationService;
import dev.louisa.jam.hub.domain.band.BandId;
import dev.louisa.jam.hub.domain.gig.GigId;
import dev.louisa.jam.hub.domain.user.UserId;
import dev.louisa.jam.hub.infrastructure.security.UserPrincipal;
import dev.louisa.jam.hub.interfaces.common.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bands")
@RequiredArgsConstructor
public class GigController {
    private final GigApplicationService gigService;

    @PostMapping("/{bandId}/gigs")
    public ResponseEntity<IdResponse> planGig(@PathVariable("bandId") UUID bandId, @RequestBody GigRequest request, @AuthenticationPrincipal UserPrincipal user) {
        final GigId gigId = gigService.planGigForBand(
                UserId.fromUUID(user.userId()),
                BandId.fromUUID(bandId), 
                request.toDetails());
        return ResponseEntity.ok(IdResponse.from(gigId));
    }
}
