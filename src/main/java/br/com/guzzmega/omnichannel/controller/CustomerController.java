package br.com.guzzmega.omnichannel.controller;

import br.com.guzzmega.omnichannel.domain.Channel;
import br.com.guzzmega.omnichannel.domain.Customer;
import br.com.guzzmega.omnichannel.domain.Interaction;
import br.com.guzzmega.omnichannel.domain.record.AssignChannelRecord;
import br.com.guzzmega.omnichannel.domain.record.CustomerRecord;
import br.com.guzzmega.omnichannel.service.ChannelService;
import br.com.guzzmega.omnichannel.service.CustomerService;
import br.com.guzzmega.omnichannel.service.InteractionService;
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

@Tag(name = "Customers")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "The Request was Successful.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))),
        @ApiResponse(responseCode = "400", description = "Invalid Request", content = @Content),
        @ApiResponse(responseCode = "404", description = "The Resource Wasn't found", content = @Content)})
@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private InteractionService interactionService;

    @Operation(summary = "Create a Customer")
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CustomerRecord customerRecord) {
        var customer = new Customer();
        BeanUtils.copyProperties(customerRecord, customer);
        customerService.save(customer);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(customer.getId()).toUri();
        return ResponseEntity.created(uri).body(customer);
    }

    @Operation(summary = "Get a Customer by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable("id") Long id){
        return ResponseEntity.ok(customerService.findById(id));
    }

    @Operation(summary = "Get all Customer")
    @GetMapping("/{id}/historic")
    public ResponseEntity<List<Interaction>> getHistoric(@PathVariable("id") Long customerId){
        return ResponseEntity.ok(interactionService.findByCustomer(customerId));
    }

    @Operation(summary = "Insert Customer into Channel")
    @PutMapping("/{id}/channels")
    public ResponseEntity<Channel> insertIntoChannel(@Valid @PathVariable("id") Long customerId, @Valid @RequestBody AssignChannelRecord record ){

        Customer customer = customerService.findById(customerId);
        Channel channel = channelService.findById(record.channelId());

        channel.getCustomerList().add(customer);
        channelService.save(channel);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(customer.getId()).toUri();
        return ResponseEntity.created(uri).body(channel);
    }
}
