package com.dot1.ticket_track.controller;


import com.dot1.ticket_track.entity.NewTypes;
import com.dot1.ticket_track.entity.mClientSPOCMaster;
import com.dot1.ticket_track.entity.mGeneralMaster;
import com.dot1.ticket_track.repository.NewtypesREpo;
import com.dot1.ticket_track.services.ClientSPOCservices;
import com.dot1.ticket_track.services.GeneralMasterServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/Clspoc")
@RestController()
public class ClientSpocMstController {



@Autowired
private ClientSPOCservices masterServices;
@PostMapping("/addMaster")
public ResponseEntity<?> addGeneral(@RequestBody mClientSPOCMaster spocmst){
    try{
        mClientSPOCMaster clientspoc = masterServices.createClientspoc(spocmst);
        if(clientspoc!=null){
            return new ResponseEntity<>(clientspoc, HttpStatus.ACCEPTED);
        }else{
            return new ResponseEntity<>( HttpStatus.NO_CONTENT);

        }

    } catch (Exception e) {
        return new ResponseEntity<>( HttpStatus.NOT_IMPLEMENTED);
    }
}

    @GetMapping("/Health")
    public String checkHealth(){
    return "All Ok";
    }

    @GetMapping("/getAllMaster")
    public ResponseEntity<?> getGeneralAll(){
        try{
            List<mClientSPOCMaster> allClientspoc = masterServices.getAllClientspoc();
            if(allClientspoc!=null){
                return new ResponseEntity<>(allClientspoc,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }


        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatus.NOT_IMPLEMENTED);

        }
    }




    @GetMapping("/findbyClientid/{gmid}")
    public ResponseEntity<?> findbyId(@PathVariable Integer gmid){
        try{
            List<mClientSPOCMaster> clientById = masterServices.getClientById(gmid);
            if(clientById!=null){
                return new ResponseEntity<>(clientById,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatus.NOT_IMPLEMENTED);

        }
    }
    @GetMapping("/findbyname/{gmType}")
    public ResponseEntity<?> findbyId(@PathVariable String gmType){
        try{
            mClientSPOCMaster mClientSPOCMaster = masterServices.getbySPCOname(gmType);
            if(mClientSPOCMaster!=null){
                return new ResponseEntity<>(mClientSPOCMaster,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatus.NOT_IMPLEMENTED);

        }
    }

//    @DeleteMapping("/deletebyid/{gmid}")
//    public ResponseEntity<?> deletebyID(@PathVariable Integer gmid){
//        try{
//            Boolean gdeletebyid = masterServices.gdeletebyid(gmid);
//            if(gdeletebyid){
//                return new ResponseEntity<>("deleted row",HttpStatus.OK);
//            }else{
//                return new ResponseEntity<>("not found",HttpStatus.NOT_FOUND);
//            }
//        } catch (Exception e) {
//            return new ResponseEntity<>( HttpStatus.NOT_IMPLEMENTED);
//
//        }
//    }



}
