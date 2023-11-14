package br.com.guzzmega.omnichannel.controller;

import br.com.guzzmega.omnichannel.domain.Channel;
import br.com.guzzmega.omnichannel.domain.Customer;
import br.com.guzzmega.omnichannel.domain.Interaction;
import br.com.guzzmega.omnichannel.domain.record.AssignChannelRecord;
import br.com.guzzmega.omnichannel.domain.record.CustomerRecord;
import br.com.guzzmega.omnichannel.service.ChannelService;
import br.com.guzzmega.omnichannel.service.CustomerService;
import br.com.guzzmega.omnichannel.service.InteractionService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private InteractionService interactionService;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CustomerRecord customerRecord) {
        var customer = new Customer();
        BeanUtils.copyProperties(customerRecord, customer);
        customerService.save(customer);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(customer.getId()).toUri();
        return ResponseEntity.created(uri).body(customer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable("id") Long id){
        return ResponseEntity.ok(customerService.findById(id));
    }

    @PutMapping("/{id}/channels")
    public ResponseEntity<Channel> insertIntoChannel(@Valid @PathVariable("id") Long customerId, @Valid @RequestBody AssignChannelRecord record ){

        Customer customer = customerService.findById(customerId);
        Channel channel = channelService.findById(record.channelId());

        channel.getCustomerList().add(customer);
        channelService.save(channel);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(customer.getId()).toUri();
        return ResponseEntity.created(uri).body(channel);
    }

    @GetMapping("/{id}/historic")
    public ResponseEntity<List<Interaction>> getHistoric(@PathVariable("id") Long customerId){
        return ResponseEntity.ok(interactionService.findByCustomer(customerId));
    }
}
