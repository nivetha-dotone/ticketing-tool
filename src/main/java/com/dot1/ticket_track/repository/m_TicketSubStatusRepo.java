package com.dot1.ticket_track.repository;

import com.dot1.ticket_track.entity.mTicketSubStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface m_TicketSubStatusRepo extends JpaRepository<mTicketSubStatus, Long> {


    @Query(value = "SELECT NEXT VALUE FOR ticketSubStatusSec AS UniqueNumber",nativeQuery = true)
    Long newIdSubTranscation();



//    CREATE SEQUENCE ticketSubStatusSec
//    AS INT
//    START WITH 980000012
//    INCREMENT BY 1
//    NO CYCLE;

}
