package br.com.guzzmega.omnichannel.repository;

import br.com.guzzmega.omnichannel.domain.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Long> {
    @Query(value = "SELECT i FROM Interaction i, Customer c WHERE i.customer.id = :customerId")
    List<Interaction> getInteractionByCustomerId(Long customerId);
}
