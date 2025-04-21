package com.dot1.ticket_track.controller;
import com.dot1.ticket_track.entity.mClientMaster;
import com.dot1.ticket_track.entity.mCompanyMaster;
import com.dot1.ticket_track.services.ClientService;
import com.dot1.ticket_track.services.CompanyService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController
{

    @Autowired
    private ClientService clientService;

    @Autowired
    private CompanyService companyService;

    @PostMapping("/addMaster")
    public ResponseEntity<?> createClient( @RequestBody mClientMaster client ) {
        try{

            mClientMaster client1 = clientService.createClient(client);
            if(client1!=null){
                return new ResponseEntity<>(client1, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/allMaster")
    public ResponseEntity<?> getAllClients() {
        try{
            List<mClientMaster> allClients = clientService.getAllClients();
            if(allClients!=null){
                return new ResponseEntity<>(allClients,HttpStatus.OK);

            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    @GetMapping("/findbyId/{clientId}")
    public ResponseEntity<?> getClientById( @PathVariable Integer clientId ) {
       try{

           mClientMaster mClientMaster = clientService.getClientById(clientId);

           if(mClientMaster!=null){
               Hibernate.initialize(mClientMaster.getCompanyMaster());
               return  new ResponseEntity<>(mClientMaster,HttpStatus.OK);
           }else{
               return  new ResponseEntity<>(HttpStatus.NOT_FOUND);

           }


       } catch (Exception e) {
           throw new RuntimeException("Client not found with ID: " + clientId);
       }


    }

    @GetMapping("/findbyClientcode/{clientcode}")
    public ResponseEntity<?> getClientById( @PathVariable String clientcode ) {
       try{

           mClientMaster mClientMaster = clientService.getClientByCode(clientcode);

           if(mClientMaster!=null){
               Hibernate.initialize(mClientMaster.getCompanyMaster());
               return  new ResponseEntity<>(mClientMaster,HttpStatus.OK);
           }else{
               return  new ResponseEntity<>(HttpStatus.NOT_FOUND);

           }


       } catch (Exception e) {
           throw new RuntimeException("Client not found with Code: " + clientcode);
       }


    }



    @GetMapping("/findbyClientName/{clientName}")
    public ResponseEntity<?> getClientByName( @PathVariable String clientName ) {
       try{

           mClientMaster mClientMaster = clientService.getClientByname(clientName).orElse(null);

           if(mClientMaster!=null){
               Hibernate.initialize(mClientMaster.getCompanyMaster());
               return  new ResponseEntity<>(mClientMaster,HttpStatus.OK);
           }else{
               return  new ResponseEntity<>(HttpStatus.NOT_FOUND);

           }


       } catch (Exception e) {
           throw new RuntimeException("Client not found with Name: " + clientName);
       }


    }


    @PutMapping("/Updatebyid/{clientId}")
    public ResponseEntity<?> updateClient( @PathVariable Integer clientId, @RequestBody mClientMaster client ) {
        try{

            mClientMaster mClientMaster = clientService.updateClient(clientId, client);

            if(mClientMaster!=null){
                return new ResponseEntity<>(mClientMaster,HttpStatus.OK);

            }else{
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    @PutMapping("/UpdatebyCode/{clientcode}")
    public ResponseEntity<?> updatebyclientcode( @PathVariable String clientcode, @RequestBody mClientMaster client ) {
        try{

            mClientMaster mClientMaster = clientService.updateByclientCode(clientcode, client);

            if(mClientMaster!=null){
                return new ResponseEntity<>(mClientMaster,HttpStatus.OK);

            }else{
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @DeleteMapping("/deletebyid/{clientId}")
    public ResponseEntity<?> deleteClient( @PathVariable Integer clientId ) {
        try{
            clientService.getClientById(clientId);
            boolean b = clientService.deleteClient(clientId);
            if(b){
                return new ResponseEntity<>("successfully deleted ",HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }
}
