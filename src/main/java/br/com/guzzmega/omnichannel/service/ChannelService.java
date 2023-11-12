package br.com.guzzmega.omnichannel.service;

import br.com.guzzmega.omnichannel.domain.Channel;
import br.com.guzzmega.omnichannel.repository.ChannelRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ChannelService {

    @Autowired
    private ChannelRepository channelRepository;

    @Transactional
    public Channel save(Channel channel){
        return channelRepository.save(channel);
    }

    public Channel findById(Long id){
        Optional<Channel> optional = channelRepository.findById(id);
        return optional.orElseThrow(() -> new ObjectNotFoundException("Channel", id));
    }

    public Channel findByEmailOrPhoneNumber(String email, String phoneNumber){
        Optional<Channel> optional = channelRepository.findChannelByEmailOrPhoneNumber(email, phoneNumber);
        return optional.orElseThrow(() -> new ObjectNotFoundException(Channel.class, email + "/" + phoneNumber));
    }

    public List<Channel> findAll(){
        return channelRepository.findAll();
    }


}
