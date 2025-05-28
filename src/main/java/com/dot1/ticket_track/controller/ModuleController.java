package com.dot1.ticket_track.controller;


import com.dot1.ticket_track.entity.mClientCMEMaster;
import com.dot1.ticket_track.entity.mClientMaster;
import com.dot1.ticket_track.entity.mEmployeeMaster;
import com.dot1.ticket_track.entity.mModulesMaster;
import com.dot1.ticket_track.services.ModuleServices;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ModulesMaster")
public class ModuleController {

    @Autowired
    private ModuleServices moduleServices;



    @GetMapping("/allMaster")
    public ResponseEntity<?> getAllClients() {
        try {
            List<mModulesMaster> allMaster = moduleServices.getAllMaster();
            if (allMaster != null) {
                return new ResponseEntity<>(allMaster, HttpStatus.OK);

            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/findbyModuleName/{mod_name}")
    public ResponseEntity<?> getModbyName(@PathVariable String mod_name) {
        try{

            mModulesMaster modules = moduleServices.getByName(mod_name);
            if(modules!=null){
                Hibernate.initialize(modules.getCompanyMaster());
                return  new ResponseEntity<>(modules,HttpStatus.OK);
            }else{
                return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new RuntimeException("Client not found with name: " + mod_name);
        } 
    }

    @GetMapping("/findbyModuleNameforTKT/{mod_name}")
    public ResponseEntity<?> getModbyNamefortkt(@PathVariable String mod_name) {
        try{

            mModulesMaster modules = moduleServices.getByNameForPassIDINTOTKT(mod_name);
            if(modules!=null){
                Hibernate.initialize(modules.getCompanyMaster());
                return  new ResponseEntity<>(modules,HttpStatus.OK);
            }else{
                return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new RuntimeException("Client not found with name: " + mod_name);
        }
    }

    @GetMapping("/findEMPbyModuleNameforTKT/{mod_name}")
    public ResponseEntity<?> getempModbyNamefortkt(@PathVariable String mod_name) {
        try{

            List<mEmployeeMaster> modules = moduleServices.getEmplistByModName(mod_name);
            if(modules!=null){
                return  new ResponseEntity<>(modules,HttpStatus.OK);
            }else{
                return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new RuntimeException("Client not found with name: " + mod_name);
        }
    }

    @GetMapping("/findCmebyModuleNameforTKT/{mod_name}")
    public ResponseEntity<?> getcmeModbyNamefortkt(@PathVariable String mod_name) {
        try{

            List<mClientCMEMaster> modules = moduleServices.getCmeByModName(mod_name);
            if(modules!=null){
                return  new ResponseEntity<>(modules,HttpStatus.OK);
            }else{
                return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new RuntimeException("Client not found with name: " + mod_name);
        }
    }



    @GetMapping("/findbyModuleId/{mod_name}")
    public ResponseEntity<?> getModbyName(@PathVariable Integer mod_name) {
        try{

            mModulesMaster modules = moduleServices.getBymodId(mod_name);
            if(modules!=null){
                Hibernate.initialize(modules.getCompanyMaster());
                return  new ResponseEntity<>(modules,HttpStatus.OK);
            }else{
                return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new RuntimeException("Client not found with name: " + mod_name);
        }
    }

    @GetMapping("/GetAllModulesbyCmpID/{cmp_id}")
    public ResponseEntity<?> getModbycmpID(@PathVariable Integer cmp_id) {
        try{
            List<mModulesMaster> modules = moduleServices.getByCmp_id(cmp_id);
            if(modules!=null){
//                Hibernate.initialize(modules.);
                return  new ResponseEntity<>(modules,HttpStatus.OK);
            }else{
                return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new RuntimeException("Client not found with company_id: " + cmp_id);
        }
    }
    @GetMapping("/GetAllModulesbyCmpName/{cmp_id}")
    public ResponseEntity<?> getModbycmpName(@PathVariable String cmp_id) {
        try{
            List<mModulesMaster> modules = moduleServices.getByCmpName(cmp_id);
            if(modules!=null){
//                Hibernate.initialize(modules.);
                return  new ResponseEntity<>(modules,HttpStatus.OK);
            }else{
                return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new RuntimeException("Client not found with company_id: " + cmp_id);
        }
    }
    @GetMapping("/GetModulesbyCmpIDPresntCME/{cmp_id}")
    public ResponseEntity<?> getModbycmpIDpresentCME(@PathVariable Integer cmp_id) {
        try{
            List<mModulesMaster> modules = moduleServices.getByCmp_idModulePresentCME(cmp_id);
            if(modules!=null){
//                Hibernate.initialize(modules.);
                return  new ResponseEntity<>(modules,HttpStatus.OK);
            }else{
                return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new RuntimeException("Client not found with company_id: " + cmp_id);
        }
    }








}

//






