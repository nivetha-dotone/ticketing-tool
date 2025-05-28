package com.dot1.ticket_track.services;



import com.dot1.ticket_track.dto.UatTransactionStroreDto;
import com.dot1.ticket_track.dto.tiketSubDto;
import com.dot1.ticket_track.dto.uatTranscationDto;
import com.dot1.ticket_track.entity.*;
import com.dot1.ticket_track.repository.GeneralMstRepo;
import com.dot1.ticket_track.repository.m_TicketSubStatusRepo;
import com.dot1.ticket_track.repository.sla_MasterRepo;
import com.dot1.ticket_track.repository.uat_TranRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class slaTranscation {

    @Autowired
    private GeneralMstRepo genMstRepo;

    @Autowired
    private TicketDservice ticketDservice;

    @Autowired
    private uat_TranRepo tranRepo;

    @Autowired
    private sla_MasterRepo slaMasterRepo;


    @Autowired
    private UatTranscationService uatTrservice;
   public Sla_Masters fetchTotalRemainingSla(Long ticketNumber){
       try{

              if(ticketNumber==null){
                return null;

              }else{
                  mTicketSdeatils getticketbyid = ticketDservice.getticketbyid(ticketNumber);
                    if(getticketbyid==null) {
                        return null;
                    }else{
                        Integer ticketType = getticketbyid.getTickettype().getGmid();
                        Integer ticketLevel = getticketbyid.getTicketlevel().getGmid();
                        Integer priority = getticketbyid.getPriority().getGmid();



                        Sla_Masters slaMasters = slaMasterRepo.findBytickettypeSalAndTicketlevelSalAndPriority(ticketType, ticketLevel, priority);
                        if(slaMasters==null){
                            return null;
                        }else{
                            Duration slaTime = slaMasters.getSlaTime();
                            //convert Duration into hours and minutes HH:mm format into integer
                            LocalDateTime CalculateSlaTime = LocalDateTime.now();
                            int hours = (int) slaTime.toHours();
                            Integer minutes = (int) slaTime.toMinutes() % 60;
                            System.out.println("Hours: " + hours);
                            System.out.println("Minutes: " + minutes);
                            List<uatTranscationDto> uatTranscations = uatTrservice.fetchAllTr(ticketNumber);
                            if(uatTranscations==null) {
                                return null;
                            }else{
                                for(uatTranscationDto uatTranscationDto : uatTranscations){
                                    if(uatTranscationDto!=null){
                                        LocalDateTime updateDate = uatTranscationDto.getUpdateDate();
                                        //convert LocalDateTime into hours and minutes HH:mm format into integer
                                        int updateHours = updateDate.getHour();


                                    }else{
                                        return null;
                                    }
                                }

                            }




                            return null;
                        }


                    }


              }


       } catch (Exception e) {
           throw new RuntimeException(e);
       }

   }





}