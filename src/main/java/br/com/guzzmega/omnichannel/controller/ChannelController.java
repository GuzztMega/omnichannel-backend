package br.com.guzzmega.omnichannel.controller;

import br.com.guzzmega.omnichannel.domain.Channel;
import br.com.guzzmega.omnichannel.domain.record.ChannelRecord;
import br.com.guzzmega.omnichannel.service.ChannelService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/channels")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @PostMapping
    public ResponseEntity<Channel> createChannel(@Valid @RequestBody ChannelRecord channelRecord) {
        var channel = new Channel();
        BeanUtils.copyProperties(channelRecord, channel);
        channelService.save(channel);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(channel.getId()).toUri();
        return ResponseEntity.created(uri).body(channel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Channel> getChannel(@PathVariable("id") Long id){
        return ResponseEntity.ok(channelService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Channel>> getAllChannels() {
        List<Channel> channels = channelService.findAll();
        return ResponseEntity.ok(channels);
    }
}
