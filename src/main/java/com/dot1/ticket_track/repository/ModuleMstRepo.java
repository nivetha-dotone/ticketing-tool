package com.dot1.ticket_track.repository;


import com.dot1.ticket_track.entity.mCompanyMaster;
import com.dot1.ticket_track.entity.mModulesMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ModuleMstRepo extends JpaRepository<mModulesMaster,Long> {
    Optional<List<mModulesMaster>> findBycompanyMaster(mCompanyMaster companyMaster);
    Optional<mModulesMaster> findBymodcode(String modname);

    @Query(value = "SELECT NEXT VALUE FOR UniqueSecModule AS UniqueNumber",nativeQuery = true)
    Long newIdMod();


//    CREATE SEQUENCE UniqueSecModule
//    AS INT
//    START WITH 101000
//    INCREMENT BY 1
//    NO CYCLE;

//   SELECT NEXT VALUE FOR UniqueSecModule AS UniqueNumber;


}
