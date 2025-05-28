//package com.dot1.ticket_track.repository;
//
//
//import com.dot1.ticket_track.entity.mTicketMaster;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface TicketMasteRepo extends JpaRepository<mTicketMaster,String> {
//
//    @Query(value = "SELECT TOP 1 t.ticketid FROM m_ticket_master t ORDER BY t.createdon DESC",nativeQuery = true)
//    String findLastTicketId();
//
//
//
//}
//
//
