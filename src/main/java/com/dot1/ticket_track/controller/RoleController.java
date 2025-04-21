package com.dot1.ticket_track.controller;

import com.dot1.ticket_track.entity.mCompanyMaster;
import com.dot1.ticket_track.entity.mRoleMaster;
import com.dot1.ticket_track.services.RoleMasterService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Role")
public class RoleController {

    @Autowired
    private RoleMasterService roleMasterService;


@PostMapping("/addRole")
public ResponseEntity<?>addRole(@RequestBody mRoleMaster roleMaster){
   try{
       mRoleMaster add = roleMasterService.add(roleMaster);
       if (add!=null){
         return new ResponseEntity<>(add, HttpStatus.OK);
       }else{
           return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
       }
   } catch (Exception e) {
       throw new RuntimeException(e);
   }
}
@GetMapping("/getAll")
public ResponseEntity<?>GetAll(){
   try{
       List<mRoleMaster> add = roleMasterService.getAll();
       if (add!=null){
         return new ResponseEntity<>(add, HttpStatus.OK);

       }else{
           return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

       }

   } catch (Exception e) {
       throw new RuntimeException(e);
   }
}
@GetMapping("/getAllActive")
public ResponseEntity<?>GetAllActive(){
   try{
       List<mRoleMaster> add = roleMasterService.getAllActive();
       if (add!=null){
         return new ResponseEntity<>(add, HttpStatus.OK);

       }else{
           return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

       }

   } catch (Exception e) {
       throw new RuntimeException(e);
   }
}


@GetMapping("/getByRole/{roleName}")
public ResponseEntity<?>findbyID(@PathVariable String roleName){
   try{
       mRoleMaster add = roleMasterService.getByroleName(roleName);
       if (add!=null){
         return new ResponseEntity<>(add, HttpStatus.OK);

       }else{
           return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

       }

   } catch (Exception e) {
       throw new RuntimeException(e);
   }
}
@PutMapping("/UpdateByID/{roleId}")
public ResponseEntity<?> updateByID(@PathVariable Integer roleId, @RequestBody mRoleMaster roleMaster){
   try{
       mRoleMaster add = roleMasterService.updatedbyID(roleId,roleMaster);
       if (add!=null){
         return new ResponseEntity<>(add, HttpStatus.OK);

       }else{
           return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

       }

   } catch (Exception e) {
       throw new RuntimeException(e);
   }
}
@DeleteMapping("/deleteByID/{roleName}")
public ResponseEntity<?> deletebyID(@PathVariable String roleName){
   try{
       Boolean b = roleMasterService.deletebyID(roleName);
       if (b){
         return new ResponseEntity<>(b, HttpStatus.OK);

       }else{
           return new ResponseEntity<>(b,HttpStatus.NOT_IMPLEMENTED);

       }
   } catch (Exception e) {
       throw new RuntimeException(e);
   }
}

    @PutMapping("{roleId}/isActive/{isActive}")
    public ResponseEntity<?> updateCompanyIsActive(
            @PathVariable Integer roleId,
            @PathVariable Boolean isActive) {

        try{
            mRoleMaster mCompanyMaster = roleMasterService.updateRoleIsActive(roleId, isActive);

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

