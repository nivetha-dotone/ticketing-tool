package com.dot1.ticket_track.repository;

import com.dot1.ticket_track.entity.mClientMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<mClientMaster, Integer> {

    Optional<mClientMaster> findByclientName(String clientName);
    Optional<mClientMaster> findByclientCode(String clientCode);


    @Query(value = "SELECT TOP(1) SUBSTRING(CONVERT(VARCHAR(36), NEWID()), 1, 5) AS UniqueCode FROM m_client_master",nativeQuery = true)
    String newIDClientcode();


    @Query(value = "SELECT NEXT VALUE FOR UniqueSecClient AS UniqueNumber",nativeQuery = true)
    Integer newIdclient();

//     CREATE SEQUENCE UniqueSecClient
//     AS INT
//     START WITH 11000
//     INCREMENT BY 1
//     NO CYCLE;
//     SELECT NEXT VALUE FOR UniqueSecTicket AS UniqueNumber;


}