package com.dot1.ticket_track.controller.consultant;


import com.dot1.ticket_track.dto.UatTransactionStroreDto;
import com.dot1.ticket_track.dto.tiketSubDto;
import com.dot1.ticket_track.dto.uatTranscationDto;
import com.dot1.ticket_track.entity.*;
import com.dot1.ticket_track.repository.AttachmentMasteRepo;
import com.dot1.ticket_track.services.EmployeeService;
import com.dot1.ticket_track.services.ModuleServices;
import com.dot1.ticket_track.services.TicketDservice;
import com.dot1.ticket_track.services.UatTranscationService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Employee")
@RequiredArgsConstructor
public class ConsultantController {
    @Autowired
    private AttachmentMasteRepo attachmentMasteRepo;
    @Autowired
    private TicketDservice ticketDservice;
    @Autowired
    private ModuleServices moduleServices;
    @Autowired
    private UatTranscationService uatTrservice;
    @Autowired
    private EmployeeService employeeService;


    @GetMapping("/getAllTicketeEMP/{compName}")
    public ResponseEntity<?> totalALLTicketMgr(@PathVariable String compName, HttpServletRequest request) {
        try {
            List<mTicketSdeatils> gettotaled = ticketDservice.getAllTicketEmp(compName,request);
            if (gettotaled != null) {
                return new ResponseEntity<>(gettotaled, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("Session expired. Please log in again.", HttpStatus.UNAUTHORIZED);

        }
    }

    @GetMapping("/getNotAssignTicketeEMP/{compName}")
    public ResponseEntity<?> totalNotAssignTicketByCompanyName(@PathVariable String compName, HttpServletRequest request) {
        try {
            List<mTicketSdeatils> gettotaled = ticketDservice.getNotAssignTicketEmp(compName,request);
            if (gettotaled != null) {
                return new ResponseEntity<>(gettotaled, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("Session expired. Please log in again.", HttpStatus.UNAUTHORIZED);

        }
    }

    @GetMapping("/getAssignTicketeEMP/{compName}")
    public ResponseEntity<?> totalAssignTicketByCompany(@PathVariable String compName, HttpServletRequest request) {
        try {
            List<mTicketSdeatils> gettotaled = ticketDservice.getAssignTicketEmp(compName,request);
            if (gettotaled != null) {
                return new ResponseEntity<>(gettotaled, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("Session expired. Please log in again.", HttpStatus.UNAUTHORIZED);

        }
    }

    @GetMapping("/getAllByModule/{modId}")
    public ResponseEntity<?> AllTicketByModule(@PathVariable Long modId, HttpServletRequest request) {
        try {
            List<mTicketSdeatils> gettotaled = ticketDservice.getAllTicketEmpByMod(modId,request);
            if (gettotaled != null) {
                return new ResponseEntity<>(gettotaled, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("Session expired. Please log in again.", HttpStatus.UNAUTHORIZED);

        }
    }

    @GetMapping("/getASsignByModule/{modId}")
    public ResponseEntity<?> AssignTicketByModule(@PathVariable Long modId, HttpServletRequest request) {
        try {
            List<mTicketSdeatils> gettotaled = ticketDservice.getAssignTicketEmpByMod(modId,request);
            if (gettotaled != null) {
                return new ResponseEntity<>(gettotaled, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("Session expired. Please log in again.", HttpStatus.UNAUTHORIZED);

        }
    }

    @GetMapping("/getNotASsignByModule/{modId}")
    public ResponseEntity<?> NotAssignTicketByModule(@PathVariable Long modId, HttpServletRequest request) {
        try {
            List<mTicketSdeatils> gettotaled = ticketDservice.getNOTAssignTicketEmpByMod(modId,request);
            if (gettotaled != null) {
                return new ResponseEntity<>(gettotaled, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("Session expired. Please log in again.", HttpStatus.UNAUTHORIZED);

        }
    }

    @GetMapping("/getAllTicketByempID/{empId}")
    public ResponseEntity<?> totalALLTicketMgr(@PathVariable Long empId, HttpServletRequest request) {
        try {
            List<mTicketSdeatils> gettotaled = ticketDservice.getAllTicketEmp(empId,request);
            if (gettotaled != null) {
                return new ResponseEntity<>(gettotaled, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("Session expired. Please log in again.", HttpStatus.UNAUTHORIZED);

        }
    }

    @GetMapping("/dropofType")
    public  ResponseEntity<?> dropdownofType(){
        try{
            List<mGeneralMaster> mGeneralMasters = ticketDservice.dropdownOFType();
            if(mGeneralMasters!=null){
                return new ResponseEntity<>(mGeneralMasters,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/dropofLevel")
    public  ResponseEntity<?> dropdownofLevel(){
        try{
            List<mGeneralMaster> mGeneralMasters = ticketDservice.dropdownOFLevel();
            if(mGeneralMasters!=null){
                return new ResponseEntity<>(mGeneralMasters,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/dropofPriority")
    public  ResponseEntity<?> dropdownofPriority(){
        try{
            List<mGeneralMaster> mGeneralMasters = ticketDservice.dropdownOFPriority();
            if(mGeneralMasters!=null){
                return new ResponseEntity<>(mGeneralMasters,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/dropofStatus")
    public  ResponseEntity<?> dropdownofStatus(){
        try{
            List<mGeneralMaster> mGeneralMasters = ticketDservice.dropdownOFStatusEMP();
            if(mGeneralMasters!=null){
                return new ResponseEntity<>(mGeneralMasters,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/downloadAttachment/{id}")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable Long id) {
        try {
            Optional<Attachment> attachmentOptional = attachmentMasteRepo.findById(id);
            if (!attachmentOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Attachment attachment = attachmentOptional.get();
            Path filePath = Paths.get(attachment.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(attachment.getFileType()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + attachment.getFileName() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/getTicketWithAttachments/{ticketid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTicketWithAttachments(@PathVariable Long ticketid, HttpServletRequest request) {
        try {
            mTicketSdeatils ticket = ticketDservice.getticketbyid(ticketid);
            List<Attachment> attachments1 = ticket.getAttachments();
            if(attachments1!=null){
                List<Attachment> passattach = new ArrayList<>();
                for(Attachment attachment: attachments1){
                    attachment.setTicketDt(null);
                    attachment.setFilePath(request.getRequestURL().toString().replace("/getTicketWithAttachments/" + ticketid, "/downloadAttachment/" + attachment.getId()));
                    passattach.add(attachment);
                }
                ticket.setAttachments(passattach);

            }else{
                ticket.setAttachments(null);
            }
            if (ticket==null) {

                return new ResponseEntity<>("Ticket not found", HttpStatus.NOT_FOUND);
            }


            return new ResponseEntity<>(ticket, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching data", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/ChangeStatusByEmp/{tcktID}/{gmid}")
    public  ResponseEntity<?> UpdateStatusByMgr(@PathVariable Long tcktID,@PathVariable Integer gmid,HttpServletRequest request ){
        try{

            mTicketSdeatils mTicketSdeatils = ticketDservice.updateStatusByMGR(tcktID, gmid, request);
            if(mTicketSdeatils!=null){
                return new ResponseEntity<>(mTicketSdeatils,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }




    }

    @PostMapping("/addTranscation")
    public ResponseEntity<?> uatTranController (@RequestBody UatTransactionStroreDto transcation){
        try{

            uatTranscationDto uatTranscation = uatTrservice.addMasterUat(transcation);
            if(uatTranscation!=null){
                return new ResponseEntity<>(uatTranscation, HttpStatus.OK);
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

    @GetMapping("/findEMPbyModuleforTKT/{mod_name}")
    public ResponseEntity<?> getempModbyfortkt(@PathVariable Long mod_name) {
        try{

            List<mEmployeeMaster> modules = moduleServices.getEmplistByModId(mod_name);
            if(modules!=null){
                return  new ResponseEntity<>(modules,HttpStatus.OK);
            }else{
                return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new RuntimeException("Client not found with name: " + mod_name);
        }
    }


    @PutMapping("/AssignEmpByEmp/{tcktID}/{empID}")
    public  ResponseEntity<?> UpdateAssignEmpByMgr(@PathVariable Long tcktID,@PathVariable Integer empID,HttpServletRequest request ){
        try{

            mTicketSdeatils mTicketSdeatils = ticketDservice.assignEmpByMGR(tcktID, empID, request);
            if(mTicketSdeatils!=null){
                return new ResponseEntity<>(mTicketSdeatils,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }




    }

//**********************************************Mail Functions


    private  final JavaMailSender mailSender;


    @GetMapping("/send")
    public String sendEmail() {
        try{

            SimpleMailMessage message=new SimpleMailMessage();
            message.setFrom("mayurinvideo29@gmail.com");
            message.setTo("mayurinvideo29@gmail.com");
            message.setSubject("Test Email");
            message.setText("This is a test email sent from Spring Boot application.");
            mailSender.send(message);
            return "Email sent successfully!";

        } catch (Exception e) {

            return e.getMessage();
        }
    }



//    ********************************
@PutMapping("/updatePassword/{username}/{pass}")
public ResponseEntity<?> updatePass(@PathVariable String username, @PathVariable String pass){
    try{
        mUserLogin_demo mUserLoginDemo = employeeService.updatePass(username, pass);
        if(mUserLoginDemo!=null){
            return new ResponseEntity<>(mUserLoginDemo,HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Username not found", HttpStatus.UNAUTHORIZED);
        }

    } catch (Exception e) {
        return new ResponseEntity<>("Username not found", HttpStatus.UNAUTHORIZED);
    }
}



}
