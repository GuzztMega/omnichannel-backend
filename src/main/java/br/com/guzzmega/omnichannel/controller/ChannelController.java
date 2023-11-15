package br.com.guzzmega.omnichannel.controller;

import br.com.guzzmega.omnichannel.domain.Channel;
import br.com.guzzmega.omnichannel.domain.record.ChannelRecord;
import br.com.guzzmega.omnichannel.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Channels")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "The Request was Successful.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Channel.class))),
        @ApiResponse(responseCode = "400", description = "Invalid Request", content = @Content),
        @ApiResponse(responseCode = "404", description = "The Resource Wasn't found", content = @Content)})
@RestController
@RequestMapping("/channels")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @Operation(summary = "Create a Channel")
    @PostMapping
    public ResponseEntity<Channel> createChannel(@Valid @RequestBody ChannelRecord channelRecord) {
        var channel = new Channel();
        BeanUtils.copyProperties(channelRecord, channel);
        channelService.save(channel);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(channel.getId()).toUri();
        return ResponseEntity.created(uri).body(channel);
    }

    @Operation(summary = "Get a Channel by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<Channel> getChannel(@PathVariable("id") Long id){
        return ResponseEntity.ok(channelService.findById(id));
    }

    @Operation(summary = "Get all Channels")
    @GetMapping
    public ResponseEntity<List<Channel>> getAllChannels() {
        List<Channel> channels = channelService.findAll();
        return ResponseEntity.ok(channels);
    }
}
