package com.dot1.ticket_track.repository;


import com.dot1.ticket_track.entity.mTicketSdeatils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TicketDRepos extends JpaRepository<mTicketSdeatils,Long> {

    @Query(value = "SELECT TOP(1) SUBSTRING(CONVERT(VARCHAR(36), NEWID()), 1, 12) AS UniqueCode FROM m_module_master",nativeQuery = true)
    String newIDTicketcode();

    @Query(value = "SELECT NEXT VALUE FOR UniqueSecTicket AS UniqueNumber",nativeQuery = true)
    Long newIdticket();

    @Query(value = "SELECT COUNT(*) FROM m_ticket_sdeatils", nativeQuery = true)
    Integer getTotalTicketDetailsCount();

    @Query(value = "SELECT count(*) FROM m_ticket_sdeatils WHERE cmexpert_id = :cmexpertId", nativeQuery = true)
    Integer findBycmexpertId(@Param("cmexpertId") Long cmexpertId);

    @Query(value = "SELECT * FROM m_ticket_sdeatils WHERE cmexpert_id = :cmexpertId ORDER BY ticketid DESC", nativeQuery = true)
    List<mTicketSdeatils> findListBycmexpertId(@Param("cmexpertId") Long cmexpertId);

    List<mTicketSdeatils> findAllByOrderByTicketidDesc();

    @Query(value = "SELECT * FROM m_ticket_sdeatils WHERE companyname = :companyname ORDER BY ticketid DESC", nativeQuery = true)
    List<mTicketSdeatils> findByCompanyname(@Param("companyname") String companyname);

 @Query(value = "SELECT * FROM m_ticket_sdeatils WHERE companyname = :companyname AND employee_id is NULL ORDER BY ticketid DESC", nativeQuery = true)
    List<mTicketSdeatils> findNOTAssignByCompanyname(@Param("companyname") String companyname);

 @Query(value = "SELECT * FROM m_ticket_sdeatils WHERE companyname = :companyname AND employee_id is NOT NULL ORDER BY ticketid DESC", nativeQuery = true)
    List<mTicketSdeatils> findAssignByCompanyname(@Param("companyname") String companyname);


    @Query(value = "SELECT * FROM m_ticket_sdeatils WHERE modulesid = :modulesid ORDER BY ticketid DESC", nativeQuery = true)
    List<mTicketSdeatils> findBymodulesid(@Param("modulesid") Long modulesid);

 @Query(value = "SELECT * FROM m_ticket_sdeatils WHERE modulesid = :modulesid AND employee_id is NULL ORDER BY ticketid DESC", nativeQuery = true)
    List<mTicketSdeatils> findNOTAssignBymodulesid(@Param("modulesid") Long modulesid);

 @Query(value = "SELECT * FROM m_ticket_sdeatils WHERE modulesid = :modulesid AND employee_id is NOT NULL ORDER BY ticketid DESC", nativeQuery = true)
    List<mTicketSdeatils> findAssignBymodulesid(@Param("modulesid") Long modulesid);


    Long countBycompanyname(String companyname);


    @Query(value = "SELECT * FROM m_ticket_sdeatils WHERE employee_id = :employeeId ORDER BY ticketid DESC", nativeQuery = true)
    List<mTicketSdeatils> findByemployeeId(@Param("employeeId") Long employeeId);





//     CREATE SEQUENCE UniqueSecTicket
//     AS INT
//     START WITH 11000000
//     INCREMENT BY 1
//     NO CYCLE;
//     SELECT NEXT VALUE FOR UniqueSecTicket AS UniqueNumber;



}
