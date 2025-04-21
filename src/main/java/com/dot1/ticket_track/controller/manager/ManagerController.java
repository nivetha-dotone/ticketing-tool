package com.dot1.ticket_track.controller.manager;


import com.dot1.ticket_track.dto.UatTransactionStroreDto;
import com.dot1.ticket_track.dto.tiketSubDto;
import com.dot1.ticket_track.dto.uatTranscationDto;
import com.dot1.ticket_track.entity.*;
import com.dot1.ticket_track.repository.AttachmentMasteRepo;
import com.dot1.ticket_track.services.ModuleServices;
import com.dot1.ticket_track.services.TicketDservice;
import com.dot1.ticket_track.services.UatTranscationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Manager")
public class ManagerController {
    @GetMapping("/get")
    public ResponseEntity<?> sayHello(){
        return ResponseEntity.ok("hii manager");
    }

    @Autowired
    private TicketDservice ticketDservice;
    @Autowired
    private AttachmentMasteRepo attachmentMasteRepo;
    @Autowired
    private ModuleServices moduleServices;
    @Autowired
    private UatTranscationService uatTrservice;

    @PostMapping(value = "/CreationTktwithD", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> CreateMasterwithAttachment( @RequestPart("tickets") String  tickets,
                                                         @RequestPart(value = "files", required = false) MultipartFile[] files) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            mTicketSdeatils ticket = objectMapper.readValue(tickets, mTicketSdeatils.class);

            mTicketSdeatils master = ticketDservice.createMaster(ticket);

            if (files != null) {
                for (MultipartFile file : files) {

                    Attachment attachment = new Attachment();
                    String originalFilename = file.getOriginalFilename();
                    String modifiedFileName = modifyFileName(originalFilename, master.getTicketcode());
                    attachment.setId(attachmentMasteRepo.newIdAttached());
                    attachment.setFileName(modifiedFileName);
                    attachment.setFileType(file.getContentType());
                    attachment.setTicketDt(master);

                    try {
                        // Save file on disk and get the path
                        String filePath1 = saveModifiedFile(file,modifiedFileName);
                        attachment.setFilePath(filePath1);
                        attachmentMasteRepo.save(attachment);

                    } catch (IOException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                }
            }

            if (master != null) {
                return new ResponseEntity<>(master, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private String getFileNameWithoutExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return fileName; // Return original if no extension
        }
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public static String getSubstringBeforeSecondDash(String input) {
        if (input == null || !input.contains("-")) {
            return input; // Return original if no dashes found
        }

        int firstDashIndex = input.indexOf("-");
        int secondDashIndex = input.indexOf("-", firstDashIndex + 1);

        if (secondDashIndex != -1) {
            return input.substring(0, secondDashIndex);
        } else {
            return input; // If less than 2 dashes, return full string
        }
    }


    private String modifyFileName(String originalFileName, String prefix) {
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileNameWithoutExtension = getFileNameWithoutExtension(originalFileName);
        String substringBeforeSecondDash = getSubstringBeforeSecondDash(prefix);
        return "T_"+substringBeforeSecondDash+fileNameWithoutExtension+fileExtension;
    }

    private String saveModifiedFile(MultipartFile file, String modifiedFileName) throws IOException {
        String directoryPath = "D:/DotOne Office Work/Project UKG WFM Java/Ticket_tracking/uploadfile/"; // Change the path as needed
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File newFile = new File(directoryPath + modifiedFileName);
        file.transferTo(newFile);
        return newFile.getAbsolutePath();

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
            List<mGeneralMaster> mGeneralMasters = ticketDservice.dropdownOFStatus();
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

    @GetMapping("/getTicketCountMgr/{compName}")
    public ResponseEntity<?> totalTicketMgr(@PathVariable String compName) {
        try {
            Long allTicketMgr = ticketDservice.gettotalTicketByMGRCompany(compName);

            if (allTicketMgr != null) {
                return new ResponseEntity<>(allTicketMgr, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("Session expired. Please log in again.", HttpStatus.UNAUTHORIZED);

        }
    }

    @GetMapping("/getAllTicketMGR/{compName}")
    public ResponseEntity<?> totalALLTicketMgr(@PathVariable String compName, HttpServletRequest request) {
        try {
            List<mTicketSdeatils> gettotaled = ticketDservice.getAllTicketMgr(compName,request);
            if (gettotaled != null) {
                return new ResponseEntity<>(gettotaled, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("Session expired. Please log in again.", HttpStatus.UNAUTHORIZED);

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

    @PutMapping("/AssignEmpByMgr/{tcktID}/{empID}")
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

    @PutMapping("/ChangeStatusByMgr/{tcktID}/{gmid}")
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

    @GetMapping("/GetAllModulesbyCmpName/{cmpName}")
    public ResponseEntity<?> getModbycmpName(@PathVariable String cmpName) {
        try{
            List<mModulesMaster> modules = moduleServices.getByCmpName(cmpName);
            if(modules!=null){
//                Hibernate.initialize(modules.);
                return  new ResponseEntity<>(modules,HttpStatus.OK);
            }else{
                return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new RuntimeException("Client not found with company Name: " + cmpName);
        }
    }

    @GetMapping("/getAllTktByModule/{modId}")
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




}
