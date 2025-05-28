package com.dot1.ticket_track.controller;

import com.dot1.ticket_track.entity.mClientCMEMaster;
import com.dot1.ticket_track.entity.mClientMaster;
import com.dot1.ticket_track.entity.mModulesMaster;
import com.dot1.ticket_track.services.clientcmeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/clientCME")
@RestController
public class CmeClientController {

    @Autowired
    private clientcmeService clientcmeService;


    @PostMapping("/addMaster")
    public ResponseEntity<?> addMastre(@RequestBody mClientCMEMaster cmeMaster){
        try {
            System.out.println("Received Request: " + new ObjectMapper().writeValueAsString(cmeMaster));

            mClientCMEMaster addcmeclient = clientcmeService.addcmeclient(cmeMaster);
            if(addcmeclient!=null){
                return new ResponseEntity<>(addcmeclient, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }


    }

        @PutMapping("/updateCme/{cmeId}")
        public ResponseEntity<?> udpdatedCme(@PathVariable Integer cmeId,@RequestBody mClientCMEMaster newCmeMaster){
        try{
            mClientCMEMaster mClientMaster = clientcmeService.updateCMEclient(cmeId, newCmeMaster);
            if(mClientMaster!=null){
                return  new ResponseEntity<>( mClientMaster,HttpStatus.OK);

            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            } catch (Exception e) {
            throw new RuntimeException(e);
        }
        }



    @GetMapping("/allMaster")
    public ResponseEntity<?> getAllClients() {
        try{
            List<mClientCMEMaster> allClients = clientcmeService.getAllClients();

            if(allClients!=null){
                return new ResponseEntity<>(allClients, HttpStatus.OK);

            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/findbyId/{cmeId}")
    public ResponseEntity<?> getClientById(@PathVariable Integer cmeId) {
        try{

            mClientCMEMaster mClientMaster = clientcmeService.getCmeById(cmeId);

            if(mClientMaster!=null){
                return  new ResponseEntity<>(mClientMaster,HttpStatus.OK);
            }else{
                return  new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }


        } catch (Exception e) {
            throw new RuntimeException("CME not found with ID: " + cmeId);
        }
    }

    @GetMapping("/findbyClientIdPcme/{cmeId}")
    public ResponseEntity<?> getClientIdPCme(@PathVariable Integer cmeId) {
        try{

            mClientMaster cmsPresentsclientByid = clientcmeService.getCMSPresentsclientByid(cmeId);

            if(cmsPresentsclientByid!=null){
                return  new ResponseEntity<>(cmsPresentsclientByid,HttpStatus.OK);
            }else{
                return  new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }
        } catch (Exception e) {
            throw new RuntimeException("CME not found with this ClientID: " + cmeId);
        }
    }

     @GetMapping("/findbyClientIdPcmeActive/{cmeId}")
    public ResponseEntity<?> getClientIdPCmeActive(@PathVariable Integer cmeId) {
        try{

            mClientMaster cmsPresentsclientByid = clientcmeService.getActiveCMSPresentsclientByid(cmeId);

            if(cmsPresentsclientByid!=null){
                return  new ResponseEntity<>(cmsPresentsclientByid,HttpStatus.OK);
            }else{
                return  new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }
        } catch (Exception e) {
            throw new RuntimeException("CME not found with this ClientID: " + cmeId);
        }
    }

     @GetMapping("/findbyClientIdPcmeInActive/{cmeId}")
    public ResponseEntity<?> getClientIdPCmeInActive(@PathVariable Integer cmeId) {
        try{

            mClientMaster cmsPresentsclientByid = clientcmeService.getInactiveCMSPresentsclientByid(cmeId);

            if(cmsPresentsclientByid!=null){
                return  new ResponseEntity<>(cmsPresentsclientByid,HttpStatus.OK);
            }else{
                return  new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }
        } catch (Exception e) {
            throw new RuntimeException("CME not found with this ClientID: " + cmeId);
        }
    }


    @GetMapping("/findALLClientPcme")
    public ResponseEntity<?> getClientsPresntCME() {
        try{

            List<mClientMaster> mClientMaster = clientcmeService.getAllCMSPresentsclient();

            if(mClientMaster!=null){
                return  new ResponseEntity<>(mClientMaster,HttpStatus.OK);
            }else{
                return  new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }


        } catch (Exception e) {
            throw new RuntimeException("Clients with CME");
        }


    }

    @GetMapping("/findbyModIdPresentcme/{modId}")
    public ResponseEntity<?> getmodIdbyCME(@PathVariable Integer modId) {
        try{

            mModulesMaster allCmeInmodule = clientcmeService.getAllCmeInmodule(modId);

            if(allCmeInmodule!=null){
                return  new ResponseEntity<>(allCmeInmodule,HttpStatus.OK);
            }else{
                return  new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }


        } catch (Exception e) {
            throw new RuntimeException("Not Found CME in this module :- ",e);
        }


    }



}
