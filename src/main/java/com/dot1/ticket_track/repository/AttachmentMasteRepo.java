package com.dot1.ticket_track.repository;


import com.dot1.ticket_track.entity.Attachment;
import com.dot1.ticket_track.entity.mTicketSdeatils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentMasteRepo extends JpaRepository<Attachment,Long> {


    List<Attachment> findByticketDt(mTicketSdeatils ticketDt);

    @Query(value = "SELECT NEXT VALUE FOR UniqueAttchment AS UniqueNumber",nativeQuery = true)
    Long newIdAttached();


}

//    CREATE SEQUENCE UniqueAttchment
//    AS INT
//    START WITH 10100000
//    INCREMENT BY 1
//    NO CYCLE;