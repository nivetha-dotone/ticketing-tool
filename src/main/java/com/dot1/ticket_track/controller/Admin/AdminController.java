package com.dot1.ticket_track.controller.Admin;

import com.dot1.ticket_track.dto.SignupRequest;
import com.dot1.ticket_track.entity.*;
import com.dot1.ticket_track.repository.AttachmentMasteRepo;
import com.dot1.ticket_track.repository.NewtypesREpo;
import com.dot1.ticket_track.services.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("/Admin")
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    private AttachmentMasteRepo attachmentMasteRepo;

    @Autowired
    private TicketDservice ticketDservice;
    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CompanyService masterServices;
    @Autowired
    private ModuleServices moduleServices;
    @Autowired
    private RoleMasterService roleMasterService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientSPOCservices masterServicespoc;
    @Autowired
    private clientcmeService clientcmeService;
    @Autowired
    private GeneralMasterServices masterServicesGM;


    //**********Drop Downs***************//
    @GetMapping("/ManagerTicket/dropofType")
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
    @PostMapping(value = "/ManagerTicket/CreationTktwithD", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
    @GetMapping(value = "/ManagerTicket/getTicketWithAttachments/{ticketid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTicketWithAttachments(@PathVariable Long ticketid, HttpServletRequest request) {
        try {
            mTicketSdeatils ticket = ticketDservice.getticketbyid(ticketid);
            if (ticket==null) {
                return new ResponseEntity<>("Ticket not found", HttpStatus.NOT_FOUND);
            }

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

            return new ResponseEntity<>(ticket, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching data", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/ManagerTicket/getAllTkt")
//    @PostMapping(value = "/Addmaster", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> GetAllTikcet(HttpServletRequest request) {
        try {

            List<mTicketSdeatils> master = ticketDservice.getallTicket(request);




            if (master != null) {
                return new ResponseEntity<>(master, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    @GetMapping("/ManagerTicket/downloadAttachment/{id}")
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

    @GetMapping("/ManagerTicket/dropofLevel")
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

    @GetMapping("/ManagerTicket/dropofPriority")
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










    @PostMapping("/General/addMaster")
    public ResponseEntity<?> addGeneral(@RequestBody mGeneralMaster generalMaster){
        try{
            mGeneralMaster mGeneralMaster = masterServicesGM.addGeneralMaster(generalMaster);
            if(mGeneralMaster!=null){
                return new ResponseEntity<>(mGeneralMaster, HttpStatus.ACCEPTED);
            }else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST );

            }

        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @GetMapping("/General/Health")
    public String checkHealth(){
        return "All Ok";
    }

    @GetMapping("/General/getAllMaster")
    public ResponseEntity<?> getGeneralAllGM(){
        try{
            List<mGeneralMaster> allGeneralMaster = masterServicesGM.getAllGeneralMaster();
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



    @GetMapping("/General/getAllTypes")
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

    @GetMapping("/General/findbyid/{gmid}")
    public ResponseEntity<?> findbyIdGM(@PathVariable Integer gmid){
        try{
            mGeneralMaster mGeneralMaster = masterServicesGM.getbyidGeneralMaster(gmid);
            if(mGeneralMaster!=null){
                return new ResponseEntity<>(mGeneralMaster,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatus.NOT_IMPLEMENTED);

        }
    }
    @GetMapping("/General/findbyname/{gmType}")
    public ResponseEntity<?> findbyId(@PathVariable String gmType){
        try{
            List<mGeneralMaster> findByname = masterServicesGM.getFindByname(gmType);
            if(findByname!=null){
                return new ResponseEntity<>(findByname,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatus.NOT_IMPLEMENTED);

        }
    }















    @PostMapping("/clientCME/addMaster")
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


    @GetMapping("/clientCME/allMaster")
    public ResponseEntity<?> getAllClientsof() {
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

    @GetMapping("/clientCME/findALLClientPcme")
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

    @GetMapping("/clientCME/findbyClientIdPcme/{cmeId}")
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

    @GetMapping("/clientCME/findbyClientIdPcmeActive/{cmeId}")
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

    @GetMapping("/clientCME/findbyClientIdPcmeInActive/{cmeId}")
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
    @GetMapping("/clientCME/findbyModIdPresentcme/{modId}")
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







    @PostMapping("/Clspoc/addMaster")
    public ResponseEntity<?> addGeneral(@RequestBody mClientSPOCMaster spocmst){
        try{
            mClientSPOCMaster clientspoc = masterServicespoc.createClientspoc(spocmst);
            if(clientspoc!=null){
                return new ResponseEntity<>(clientspoc, HttpStatus.ACCEPTED);
            }else{
                return new ResponseEntity<>( HttpStatus.NO_CONTENT);

            }

        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatus.NOT_IMPLEMENTED);
        }
    }
    @GetMapping("/clients/findbyClientcode/{clientcode}")
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
    @GetMapping("/clients/allMaster")
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
    @PostMapping("/clients/addMaster")
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
    @GetMapping("/clients/findbyClientName/{clientName}")
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
    @GetMapping("/Employee/getAll")
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
    @PostMapping("/Employee/addEmployee")
    @Transactional
    public ResponseEntity<?> addEmployee(@RequestBody mEmployeeMaster employee) {
        try {
            mEmployeeMaster adduserlogin = employeeService.addEmployee(employee);
//            if(adduserlogin!=null){
//                SignupRequest signupRequest = new SignupRequest();
//                signupRequest.setUserID(adduserlogin.getEmailId());
//                signupRequest.setUserPWD("Dot1@12345");
//                signupRequest.setIsActLog(false);
//                mUserLogin_demo signup = authenticationService.signup(signupRequest);
                if(adduserlogin !=null){
                    return new ResponseEntity<>(adduserlogin, HttpStatus.OK);

                }else{
                    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
                }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/Employee/getByempCode/{empCode}")
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
    @GetMapping("/Employee/EmpName/{empName}")
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
    @PostMapping("/Role/addRole")
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
    @GetMapping("/Role/getAll")
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
    @GetMapping("/Role/getAllActive")
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
    @GetMapping("/Role/getByRole/{roleName}")
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
    @GetMapping("/ModulesMaster/findbyModuleId/{mod_name}")
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
    @PostMapping("/ModulesMaster/Addmaster")
    public ResponseEntity<?> CreateMaster(@RequestBody mModulesMaster ModulesMaster) {
        try {

            mModulesMaster master = moduleServices.createMaster(ModulesMaster);
            if (master != null) {
                return new ResponseEntity<>(master, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/ModulesMaster/allMaster")
    public ResponseEntity<?> getAllModule() {
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

    @GetMapping("/ModulesMaster/findbyModuleName/{mod_name}")
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

    @GetMapping("/ModulesMaster/GetAllModulesbyCmpID/{cmp_id}")
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
    @GetMapping("/ModulesMaster/findbyModuleNameforTKT/{mod_name}")
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
    @GetMapping("/ModulesMaster/findEMPbyModuleNameforTKT/{mod_name}")
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

    @GetMapping("/Company/findclientbycomp/{CMPID}")
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
    @GetMapping("/Company/findbyid/{CMPID}")
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

    @GetMapping("/Company/getAllActive")
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

    @GetMapping("/Company/getAllMaster")
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

    @PutMapping("/Company/updateComp/{CMPID}")
    public  ResponseEntity<?> updateCompany(@PathVariable Integer CMPID, @RequestBody mCompanyMaster company) {

        try{
            mCompanyMaster mCompanyMaster = masterServices.updateCompany(CMPID, company);
            if(mCompanyMaster!=null){
                return new ResponseEntity<>(mCompanyMaster,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/Company/addMaster")
    public ResponseEntity<?> addGeneral(@RequestBody mCompanyMaster compMaster){
        try{
            mCompanyMaster mGeneralMaster = masterServices.createCompany(compMaster);
            if(mGeneralMaster!=null){
                return new ResponseEntity<>(mGeneralMaster, HttpStatus.ACCEPTED);
            }else{
                return new ResponseEntity<>( HttpStatus.NO_CONTENT);

            }

        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @GetMapping("/hello")
    public ResponseEntity<?> sayHello(){
        return ResponseEntity.ok("hii admin");
    }

    @GetMapping("/getTotalTicket")
    public ResponseEntity<?> totalTicket() {
        try {
            Integer gettotaled = ticketDservice.gettotalTicket();
            if (gettotaled != null) {
                return new ResponseEntity<>(gettotaled, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("Session expired. Please log in again.", HttpStatus.UNAUTHORIZED);

        }
    }


//    @GetMapping("/")




}
