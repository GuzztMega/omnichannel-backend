package br.com.guzzmega.omnichannel.controller;

import br.com.guzzmega.omnichannel.domain.Interaction;
import br.com.guzzmega.omnichannel.domain.InteractionStatus;
import br.com.guzzmega.omnichannel.domain.record.InteractionRecord;
import br.com.guzzmega.omnichannel.service.InteractionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/interactions")
public class InteractionController {

    @Autowired
    private InteractionService interactionService;

    @PostMapping
    public ResponseEntity<Interaction> createInteraction(@Valid @RequestBody InteractionRecord record) {
        Interaction interaction = interactionService.enqueueInteraction(record);
        if(interaction.getInteractionStatus() == InteractionStatus.FAILED)
        {
            return ResponseEntity.notFound().build();
        }

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(interaction.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Interaction> getInteraction(@PathVariable("id") Long id){
        return ResponseEntity.ok(interactionService.findById(id));
    }
}
