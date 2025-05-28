package com.dot1.ticket_track.controller;


import com.dot1.ticket_track.dto.UatTransactionStroreDto;
import com.dot1.ticket_track.dto.tiketSubDto;
import com.dot1.ticket_track.dto.uatTranscationDto;
import com.dot1.ticket_track.services.UatTranscationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/slaController")
public class slaController {

    @Autowired
    private UatTranscationService uatTrservice;

//  @GetMapping("/getSlaTimeing")
//    public ResponseEntity<?> uatTranController (@RequestBody UatTransactionStroreDto transcation){
//        try{
//
//            uatTranscationDto uatTranscationDto = uatTrservice.addMasterUat(transcation);
//
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }











}
