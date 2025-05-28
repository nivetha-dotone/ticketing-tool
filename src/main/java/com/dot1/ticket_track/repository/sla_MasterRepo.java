package com.dot1.ticket_track.repository;

import com.dot1.ticket_track.entity.Sla_Masters;
import com.dot1.ticket_track.entity.uatTranscation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface sla_MasterRepo extends JpaRepository<Sla_Masters, Long> {


    @Query(value = "SELECT NEXT VALUE FOR sla_MasterSec AS UniqueNumber",nativeQuery = true)
    Long newIdsla_Master();

    @Query(value = "SELECT * FROM sla_masters WHERE tickettype_sal = :tickettypeSal AND ticketlevel_sal = :ticketlevelSal AND priority_sal = :priority", nativeQuery = true)
    Sla_Masters findBytickettypeSalAndTicketlevelSalAndPriority(@Param("tickettypeSal") Integer tickettypeSal, @Param("ticketlevelSal") Integer ticketlevelSal, @Param("priority") Integer priority);



//    CREATE SEQUENCE sla_MasterSec
//    AS BIGINT
//    START WITH 7864548
//    INCREMENT BY 1
//    NO CYCLE;





}
