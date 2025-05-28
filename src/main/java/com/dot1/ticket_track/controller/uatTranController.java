package com.dot1.ticket_track.controller;


import com.dot1.ticket_track.dto.UatTransactionStroreDto;
import com.dot1.ticket_track.dto.tiketSubDto;
import com.dot1.ticket_track.dto.uatTranscationDto;
import com.dot1.ticket_track.entity.uatTranscation;
import com.dot1.ticket_track.services.UatTranscationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/uatController")
public class uatTranController {

    @Autowired
    private UatTranscationService uatTrservice;

    @PostMapping("/addTranscation")
    public ResponseEntity<?> uatTranController (@RequestBody UatTransactionStroreDto transcation){
        try{

            uatTranscationDto uatTranscationDto = uatTrservice.addMasterUat(transcation);
            if(uatTranscationDto!=null){
                return new ResponseEntity<>(uatTranscationDto, HttpStatus.OK);
            }else{
                return  new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getTranscation/{tkid}")
    public ResponseEntity<?> getTicketTranscation (@PathVariable Long tkid){
        try{
            List<uatTranscationDto> uatTranscations = uatTrservice.fetchAllTr(tkid);
            if(uatTranscations!=null){
                return new ResponseEntity<>(uatTranscations, HttpStatus.OK);
            }else{
                return  new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/dropdownOFsubStatus")
    public ResponseEntity<?> getTicketTranscation (){
        try{
            List<tiketSubDto> allSubstatus = uatTrservice.getAllSubstatus();
            if(allSubstatus!=null){
                return new ResponseEntity<>(allSubstatus, HttpStatus.OK);
            }else{
                return  new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }










}
