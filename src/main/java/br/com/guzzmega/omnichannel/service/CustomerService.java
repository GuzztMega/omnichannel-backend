package br.com.guzzmega.omnichannel.service;

import br.com.guzzmega.omnichannel.domain.ChannelType;
import br.com.guzzmega.omnichannel.domain.Customer;
import br.com.guzzmega.omnichannel.repository.CustomerRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public Customer save(Customer customer){
        return customerRepository.save(customer);
    }

    public Customer findById(Long id){
        Optional<Customer> optional = customerRepository.findById(id);
        return optional.orElseThrow(() -> new ObjectNotFoundException("Customer", id));
    }

    public Customer findByChannelType(String param, ChannelType channelType){
        Optional<Customer> optional;

        switch(channelType)
        {
            case CHAT:
            case EMAIL:
                optional = customerRepository.findCustomerByEmail(param);
                break;
            default:
                optional = customerRepository.findCustomerByPhoneNumber(param);
                break;
        }

        return optional.orElseThrow(() -> new ObjectNotFoundException(Customer.class, param));
    }

    public List<Customer> findAll(){
        return customerRepository.findAll();
    }


}
