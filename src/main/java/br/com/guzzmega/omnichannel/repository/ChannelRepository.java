package br.com.guzzmega.omnichannel.repository;

import br.com.guzzmega.omnichannel.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {
    Optional<Channel> findChannelByEmailOrPhoneNumber(String email, String phoneNumber);
}
