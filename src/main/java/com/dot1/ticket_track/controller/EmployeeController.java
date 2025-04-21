package com.dot1.ticket_track.controller;

import com.dot1.ticket_track.entity.mEmployeeMaster;
import com.dot1.ticket_track.services.EmployeeService;
import com.dot1.ticket_track.services.TicketDservice;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    @PostMapping("/addEmployee")
    public ResponseEntity<?> addEmployee(@RequestBody mEmployeeMaster employee) {
        try {
            return new ResponseEntity<>(employeeService.addEmployee(employee), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllEmployees() {
        try {
            List<mEmployeeMaster> allEmployees = employeeService.getAllEmployees();
            if(allEmployees!=null){
                return new ResponseEntity<>(allEmployees, HttpStatus.OK);
            }else{
                return new ResponseEntity<>( HttpStatus.NOT_FOUND);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getById/{empId}")
    public ResponseEntity<?> getEmployeeById(@PathVariable int empId) {
        try {
            mEmployeeMaster employeeById = employeeService.getEmployeeById(empId);
            if(employeeById!=null){
                return new ResponseEntity<>(employeeById,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
//
//    @GetMapping("/getByModid/{empId}")
//    public ResponseEntity<?> getemployeebymodid(@PathVariable Integer empId) {
//        try {
//            List<mEmployeeMaster> byModID = employeeService.getByModID(empId);
//            if(byModID!=null){
//                return new ResponseEntity<>(byModID,HttpStatus.OK);
//            }else{
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
    @GetMapping("/Employee/{empName}")
    public ResponseEntity<?> getempName(@PathVariable String empName) {
        try {
            List<mEmployeeMaster> byempName = employeeService.getByempName(empName);
            if(byempName!=null){
                return new ResponseEntity<>(byempName,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/getByempCode/{empCode}")
    public ResponseEntity<?> getempCode(@PathVariable String empCode) {
        try {
            mEmployeeMaster byempCode = employeeService.getByempCode(empCode);
            if(byempCode!=null){
                return new ResponseEntity<>(byempCode,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/updateById/{empId}")
    public ResponseEntity<?> updateEmployee(@PathVariable int empId, @RequestBody mEmployeeMaster employee) {
        try {
            mEmployeeMaster updated = employeeService.updateEmployee(empId, employee);
            return updated != null ? new ResponseEntity<>(updated, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/deleteById/{empId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable int empId) {
        try {
            boolean deleted = employeeService.deleteEmployee(empId);
            return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
