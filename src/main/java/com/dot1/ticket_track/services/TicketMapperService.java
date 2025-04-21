//package com.dot1.ticket_track.services;
//
//import com.dot1.ticket_track.entity.mTicketDetailsDTO;
//import com.dot1.ticket_track.entity.mTicketSdeatils;
//import org.springframework.stereotype.Service;
//
//@Service
//public class TicketMapperService {
//
//    public mTicketDetailsDTO convertToDTO(mTicketSdeatils ticket) {
//        mTicketDetailsDTO dto = new mTicketDetailsDTO();
//        dto.setTicketid(ticket.getTicketid());
//        dto.setTicketcode(ticket.getTicketcode());
//        dto.setTickettype(ticket.getTickettype().getGmid());
//        dto.setTicketlevel(ticket.getTicketlevel().getGmid());
//        dto.setPriority(ticket.getPriority().getGmid());
//        dto.setCompanyname(ticket.getCompanyname());
//        dto.setClientid(ticket.getClientid().getClientId());
//        dto.setTicketnote(ticket.getTicketnote());
//        dto.setTicketdesc(ticket.getTicketdesc());
//        dto.setModulesid(ticket.getModulesid().getModId());
//        dto.setCmexpertId(ticket.getCmexpertId().getCmeId());
//        dto.setEmployeeId(ticket.getEmployeeId().getEmpId());
//        dto.setReportedon(ticket.getReportedon());
//        dto.setCreatedon(ticket.getCreatedon());
//        return dto;
//    }
//}
