package com.sndp.agil.backend.repository;

import com.sndp.agil.backend.model.StatutTicket;
import com.sndp.agil.backend.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByGuichetIdAndStatut(Long guichetId, StatutTicket statut);

    @Query("SELECT t FROM Ticket t WHERE t.guichet.id = :guichetId AND DATE(t.dateCreation) = CURRENT_DATE ORDER BY t.dateCreation")
    List<Ticket> findTodayTicketsByGuichet(@Param("guichetId") Long guichetId);

}