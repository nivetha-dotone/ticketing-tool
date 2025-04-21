package com.dot1.ticket_track.repository;

import com.dot1.ticket_track.entity.mClientMaster;
import com.dot1.ticket_track.entity.mClientSPOCMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SpocMstRepo extends JpaRepository<mClientSPOCMaster,Integer> {
    Optional<List<mClientSPOCMaster>> findByclientmasterId(mClientMaster clientmasterId);

    Optional<mClientSPOCMaster> findByspocName(String spocName);

    @Query(value = "SELECT NEXT VALUE FOR UniqueSecSPOC AS UniqueNumber",nativeQuery = true)
    Integer newIdSPOC();


//
//     CREATE SEQUENCE UniqueSecSPOC
//     AS INT
//     START WITH 70000
//     INCREMENT BY 1
//     NO CYCLE;
//     SELECT NEXT VALUE FOR UniqueSecSPOC AS UniqueNumber;



}
