package com.dot1.ticket_track.controller;


import com.dot1.ticket_track.entity.mClientMaster;
import com.dot1.ticket_track.entity.mCompanyMaster;
import com.dot1.ticket_track.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:3001")
@RequestMapping("/Company")
@RestController()
public class CompMasterController {



@Autowired
private CompanyService masterServices;


    @GetMapping("/getAllMaster")
    public ResponseEntity<?> getGeneralAll(){
        try{
            List<mCompanyMaster> allGeneralMaster = masterServices.getAllCompanies();

            if(allGeneralMaster!=null){

                return new ResponseEntity<>(allGeneralMaster,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }


        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatus.NOT_IMPLEMENTED);

        }
    }


    @GetMapping("/getAllActive")
    public ResponseEntity<?> getGeneralAllActive(){
        try{
            List<mCompanyMaster> allGeneralMaster = masterServices.getAllActive();

            if(allGeneralMaster!=null){

                return new ResponseEntity<>(allGeneralMaster,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }


        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatus.NOT_IMPLEMENTED);

        }
    }

    @GetMapping("/findbyid/{CMPID}")
    public ResponseEntity<?> findbyId(@PathVariable Integer CMPID){
        try{
            mCompanyMaster mGeneralMaster = masterServices.getCompanyById(CMPID);
            if(mGeneralMaster!=null){
                return new ResponseEntity<>(mGeneralMaster,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatus.NOT_IMPLEMENTED);

        }
    }


    @GetMapping("/findclientbycomp/{CMPID}")
    public ResponseEntity<?> findclientbyId(@PathVariable Integer CMPID){
        try{
            List<mClientMaster> companyByIdclient = masterServices.getCompanyByIdclient(CMPID);
            if(companyByIdclient!=null){
                return new ResponseEntity<>(companyByIdclient,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatus.NOT_IMPLEMENTED);

        }
    }


    @PutMapping("/isActive/{companyId}")
    public ResponseEntity<mCompanyMaster> updateCompanyIsActive(
            @PathVariable Integer companyId,
            @RequestParam Boolean isActive) {

      try{
          mCompanyMaster mCompanyMaster = masterServices.updateCompanyIsActive(companyId, isActive);

          if(mCompanyMaster!=null){
              return new ResponseEntity<>(mCompanyMaster,HttpStatus.OK);
          }else {
              return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

          }

      } catch (Exception e) {
          throw new RuntimeException(e);
      }

    }







}
