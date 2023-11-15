package br.com.guzzmega.omnichannel.controller;

import br.com.guzzmega.omnichannel.domain.Interaction;
import br.com.guzzmega.omnichannel.domain.InteractionStatus;
import br.com.guzzmega.omnichannel.domain.record.InteractionRecord;
import br.com.guzzmega.omnichannel.service.InteractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Interactions")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "The Request was Successful.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Interaction.class))),
        @ApiResponse(responseCode = "400", description = "Invalid Request", content = @Content),
        @ApiResponse(responseCode = "404", description = "The Resource Wasn't found", content = @Content)})
@RestController
@RequestMapping("/interactions")
public class InteractionController {

    @Autowired
    private InteractionService interactionService;

    @Operation(summary = "Create a Interaction")
    @PostMapping
    public ResponseEntity<Interaction> createInteraction(@Valid @RequestBody InteractionRecord interactionRecord) {
        Interaction interaction = interactionService.enqueueInteraction(interactionRecord);
        if(interaction.getInteractionStatus() == InteractionStatus.FAILED)
            return ResponseEntity.notFound().build();

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(interaction.getId()).toUri();
        return ResponseEntity.created(uri).body(interaction);
    }

    @Operation(summary = "Get a Interaction by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<Interaction> getInteraction(@PathVariable("id") Long id){
        return ResponseEntity.ok(interactionService.findById(id));
    }
}
