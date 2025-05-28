package com.dot1.ticket_track.controller;


import com.dot1.ticket_track.entity.NewTypes;
import com.dot1.ticket_track.entity.mGeneralMaster;
import com.dot1.ticket_track.repository.NewtypesREpo;
import com.dot1.ticket_track.services.GeneralMasterServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RequestMapping("/General")
@RestController()
public class GeneralMasterController {



@Autowired
private GeneralMasterServices masterServices;
@PostMapping("/addMaster")
public ResponseEntity<?> addGeneral(@RequestBody mGeneralMaster generalMaster){
    try{
         mGeneralMaster mGeneralMaster = masterServices.addGeneralMaster(generalMaster);
        if(mGeneralMaster!=null){
            return new ResponseEntity<>(mGeneralMaster, HttpStatus.ACCEPTED);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST );

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
            List<mGeneralMaster> allGeneralMaster = masterServices.getAllGeneralMaster();
            if(allGeneralMaster!=null){
                return new ResponseEntity<>(allGeneralMaster,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }


        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatus.NOT_IMPLEMENTED);

        }
    }

    @Autowired
    private NewtypesREpo newtypesREpo;



    @GetMapping("/getAllTypes")
    public ResponseEntity<?> getALlType(){
        try{

            List<NewTypes> allGeneralMaster = newtypesREpo.findAll();

            if(allGeneralMaster!=null){
                return new ResponseEntity<>(allGeneralMaster,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }


        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatus.NOT_IMPLEMENTED);

        }
    }

    @GetMapping("/findbyid/{gmid}")
    public ResponseEntity<?> findbyId(@PathVariable Integer gmid){
        try{
            mGeneralMaster mGeneralMaster = masterServices.getbyidGeneralMaster(gmid);
            if(mGeneralMaster!=null){
                return new ResponseEntity<>(mGeneralMaster,HttpStatus.OK);
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
            List<mGeneralMaster> findByname = masterServices.getFindByname(gmType);
            if(findByname!=null){
                return new ResponseEntity<>(findByname,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatus.NOT_IMPLEMENTED);

        }
    }

    @DeleteMapping("/deletebyid/{gmid}")
    public ResponseEntity<?> deletebyID(@PathVariable Integer gmid){
        try{
            Boolean gdeletebyid = masterServices.gdeletebyid(gmid);
            if(gdeletebyid){
                return new ResponseEntity<>("deleted row",HttpStatus.OK);
            }else{
                return new ResponseEntity<>("not found",HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatus.NOT_IMPLEMENTED);

        }
    }



}
