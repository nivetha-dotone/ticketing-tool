package com.dot1.ticket_track.repository;



import com.dot1.ticket_track.entity.mCompanyMaster;
import com.dot1.ticket_track.entity.mTicketSdeatils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyRepository extends JpaRepository<mCompanyMaster,Integer> {

    @Query(value = "SELECT NEXT VALUE FOR UniqueSecCMP AS UniqueNumber",nativeQuery = true)
    Integer newIdCMP();

    @Query(value = "SELECT * FROM m_company_master WHERE cmpnm = :companyname", nativeQuery = true)
    mCompanyMaster findByCompanyname(@Param("companyname") String companyname);



//    CREATE SEQUENCE UniqueSecCMP
//    AS INT
//    START WITH 1100
//    INCREMENT BY 1
//    NO CYCLE;


}
