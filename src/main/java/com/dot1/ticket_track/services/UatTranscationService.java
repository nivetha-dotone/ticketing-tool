package com.dot1.ticket_track.services;



import com.dot1.ticket_track.dto.UatTransactionStroreDto;
import com.dot1.ticket_track.dto.tiketSubDto;
import com.dot1.ticket_track.dto.uatTranscationDto;
import com.dot1.ticket_track.entity.*;
import com.dot1.ticket_track.repository.GeneralMstRepo;
import com.dot1.ticket_track.repository.m_TicketSubStatusRepo;
import com.dot1.ticket_track.repository.uat_TranRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@Service
public class UatTranscationService {

    @Autowired
    private GeneralMstRepo genMstRepo;


    @Autowired
    private uat_TranRepo tranRepo;

    @Autowired
    private m_TicketSubStatusRepo statusRepo;

   public uatTranscationDto addMasterUat(UatTransactionStroreDto transcation){
       try{
           uatTranscation newAddTran = new uatTranscation();
           newAddTran.setUatId(tranRepo.newIduatTranscation());
           newAddTran.setComment(transcation.getCommentDto());

           mTicketSdeatils ticketNumber = new mTicketSdeatils();
           ticketNumber.setTicketid(transcation.getTicket_numberDto());

           newAddTran.setTicket_number(ticketNumber);
           if(transcation.getCmeexpertDto()!=null) {
               mClientCMEMaster cmexpertId = new mClientCMEMaster();

               cmexpertId.setCmeId(transcation.getCmeexpertDto());
               newAddTran.setCmexpertId(cmexpertId);
           }else{
               newAddTran.setCmexpertId(null);

           }

           if(transcation.getEmployeeDto()!=null) {
               mEmployeeMaster employeeId = new mEmployeeMaster();
               employeeId.setEmpId(transcation.getEmployeeDto());
               newAddTran.setEmployeeId(employeeId);
           }else{
               newAddTran.setEmployeeId(null);
           }

           mGeneralMaster statusAdd = new mGeneralMaster();
           statusAdd.setGmid(transcation.getStatusDto());
           newAddTran.setStatus(statusAdd);

           Integer statusDto = transcation.getStatusDto();
           mGeneralMaster statuscheckHold = genMstRepo.findById(statusDto).orElse(null);
           if(statuscheckHold!=null){
               String gmDescription = statuscheckHold.getGmDescription();
               if(gmDescription.equals("ON-HOLD")){
                   mTicketSubStatus subStatus =new mTicketSubStatus();
                   subStatus.setSubId(transcation.getSub_statusDto());
                    newAddTran.setSub_status(subStatus);



               }else{
                   newAddTran.setSub_status(null);
               }
           }else{
            newAddTran.setSub_status(null);
           }
           uatTranscation check = tranRepo.save(newAddTran);
           uatTranscationDto passTr =new uatTranscationDto();
           passTr.setUaId(check.getUatId());
           passTr.setTicket_numberDto(null);
           passTr.setCommentDto(check.getComment());
           passTr.setTicket_numberDto(check.getTicket_number().getTicketid());
           mTicketSubStatus subStatus = check.getSub_status();
           if(subStatus!=null){
               tiketSubDto pass= new tiketSubDto();
               pass.setSubId(subStatus.getSubId());
               mGeneralMaster status = subStatus.getStatus();
               if(status!=null){
                   pass.setStatus(status.getGmDescription());
               }else{
                   pass.setStatus(null);

               }
               mGeneralMaster subStatuspa = subStatus.getSub_status();
               if(subStatuspa!=null){
                   pass.setSub_status(subStatuspa.getGmDescription());

               }else{
                   pass.setSub_status(null);
               }
               passTr.setSub_statusDto(pass);

           }else{
               passTr.setSub_statusDto(null);

           }
           if(check.getInsertdtmUat()!=null){
               passTr.setUpdateDate(check.getInsertdtmUat());

           }else{
               passTr.setUpdateDate(null);
           }



           if(passTr!=null){
               return passTr;
           }else{
               return  null;
           }
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
   }

   public List<uatTranscationDto> fetchAllTr(Long ticketNumber){
       try{

           List<uatTranscation> byticketNumber = tranRepo.findByticket_number(ticketNumber);
           if(byticketNumber!=null){
               List<uatTranscationDto> transcations=new ArrayList<>();

               for(uatTranscation check: byticketNumber){
                   uatTranscationDto passTr =new uatTranscationDto();
                    passTr.setUaId(check.getUatId());
                    passTr.setCommentDto(check.getComment());
                    passTr.setTicket_numberDto(check.getTicket_number().getTicketid());
                   mTicketSubStatus subStatus = check.getSub_status();
                   if(subStatus!=null){
                       tiketSubDto pass= new tiketSubDto();
                       pass.setSubId(subStatus.getSubId());
                       mGeneralMaster status = subStatus.getStatus();
                       if(status!=null){
                           pass.setStatus(status.getGmDescription());
                       }else{
                           pass.setStatus(null);

                       }
                       mGeneralMaster subStatuspa = subStatus.getSub_status();
                       if(subStatuspa!=null){
                           pass.setSub_status(subStatuspa.getGmDescription());

                       }else{
                           pass.setSub_status(null);
                       }
                       passTr.setSub_statusDto(pass);

                   }else{
                       passTr.setSub_statusDto(null);

                   }
                   mEmployeeMaster employeeId = check.getEmployeeId();
                   if(employeeId!=null){
                       passTr.setEmployeeDto(employeeId.getEmpCode());
                       passTr.setEmpNameDto(employeeId.getEmpName());

                   }else if(check.getCmexpertId()!=null){
                       mClientCMEMaster cmexpertId = check.getCmexpertId();
                       passTr.setCmeexpertDto(cmexpertId.getCmeId());
                       passTr.setCmeNameDto(cmexpertId.getCmeName());
                   }else{
                    passTr.setCmeexpertDto(null);
                    passTr.setCmeNameDto(null);
                   }
                   mGeneralMaster status = check.getStatus();
                   if(status!=null){
                       passTr.setStatusId(status.getGmid());
                       passTr.setStatusName(status.getGmDescription());
                   }else{
                       passTr.setStatusId(null);
                       passTr.setStatusName(null);
                   }

                   if(check.getInsertdtmUat()!=null){
                       passTr.setUpdateDate(check.getInsertdtmUat());

                   }else{
                       passTr.setUpdateDate(null);
                   }



                   transcations.add(passTr);

               }



               return transcations;






           }else{
               return null;
           }


       } catch (Exception e) {
           throw new RuntimeException(e);
       }
   }

   public  List<tiketSubDto> getAllSubstatus(){
       try{

           List<mTicketSubStatus> all = statusRepo.findAll();
           if(all!=null || !all.isEmpty()){
               List<tiketSubDto> passSub= new ArrayList<>();
               for(mTicketSubStatus check:all){

                   tiketSubDto pass= new tiketSubDto();
                   pass.setSubId(check.getSubId());
                   mGeneralMaster status = check.getStatus();
                   if(status!=null){
                       pass.setStatus(status.getGmDescription());
                   }else{
                       pass.setStatus(null);

                   }
                   mGeneralMaster subStatus = check.getSub_status();
                   if(subStatus!=null){
                       pass.setSub_status(subStatus.getGmDescription());

                   }else{
                       pass.setSub_status(null);
                   }



                passSub.add(pass);
               }
                return passSub;


           }else{
               return null;
           }


       } catch (Exception e) {
           throw new RuntimeException(e);
       }
   }



}