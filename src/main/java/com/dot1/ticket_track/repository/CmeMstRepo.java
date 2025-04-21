package com.dot1.ticket_track.repository;

import com.dot1.ticket_track.entity.mClientCMEMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface    CmeMstRepo extends JpaRepository<mClientCMEMaster,Integer> {

    @Query(value = "SELECT NEXT VALUE FOR UniqueNumberEMP AS UniqueNumber",nativeQuery = true)
    Integer newcmeID();

}
//CREATE SEQUENCE UniqueNumberEMP
//AS INT
//START WITH 10000000
//INCREMENT BY 1
//NO CYCLE;
//
//SELECT NEXT VALUE FOR UniqueNumberSequence AS UniqueNumber;
