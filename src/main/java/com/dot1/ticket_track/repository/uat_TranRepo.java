package com.dot1.ticket_track.repository;

import com.dot1.ticket_track.entity.mTicketSdeatils;
import com.dot1.ticket_track.entity.mUserLogin_demo;
import com.dot1.ticket_track.entity.uatTranscation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface uat_TranRepo extends JpaRepository<uatTranscation, Long> {


    @Query(value = "SELECT NEXT VALUE FOR uatTranscationSec AS UniqueNumber",nativeQuery = true)
    Long newIduatTranscation();


    @Query(value = "SELECT * FROM uat_transcation WHERE ticket_number = :ticket_number ORDER BY uat_id DESC", nativeQuery = true)
    List<uatTranscation> findByticket_number(@Param("ticket_number") Long ticket_number);


//    CREATE SEQUENCE uatTranscationSec
//    AS BIGINT
//    START WITH 700001000
//    INCREMENT BY 1
//    NO CYCLE;





}
