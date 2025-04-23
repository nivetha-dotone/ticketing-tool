package com.dot1.ticket_track.services;


import com.dot1.ticket_track.controller.mailTrail.Emailcontroller;
import com.dot1.ticket_track.entity.*;
import com.dot1.ticket_track.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class TicketDservice {

    @Autowired
    private TicketDRepos ticketDRepos;
    @Autowired
    private GeneralMasterServices masterServices;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private GeneralMasterServices masterServicesGM;
    @Autowired
    private Emailcontroller emailcontroller;
    @Autowired
    private ModuleServices moduleServices;


    public mTicketSdeatils createMaster(mTicketSdeatils mTicketSdeatils, MultipartFile[] files) {
        try{
            if(mTicketSdeatils!=null) {

                mTicketSdeatils.setTicketid(ticketDRepos.newIdticket());
                String prefix = ticketDRepos.newIDTicketcode();
                String TiktCode;
                Integer gmid = mTicketSdeatils.getTickettype().getGmid();
                mGeneralMaster toCheckGm = masterServices.getbyidGeneralMaster(gmid);
                String gmDescription = toCheckGm.getGmDescription();
                if(gmDescription.equals("SERVICE REQUEST")){
                     TiktCode ="SR-"+prefix;
                } else if (gmDescription.equals("INCIDENT REQUEST")) {
                    TiktCode="IR-"+prefix;
                } else if (gmDescription.equals("CHANGE REQUEST")) {
                    TiktCode="CR-"+prefix;
                }else{
                    TiktCode=null;

                }

                mTicketSdeatils.setTicketcode(TiktCode);
                mGeneralMaster topassDefaultStatus= new mGeneralMaster();
//                ********************
//                Search GmStatus ID Manual form DB and put it here
//


                topassDefaultStatus.setGmid(26);
                mTicketSdeatils.setStatus(topassDefaultStatus);





                mTicketSdeatils save = ticketDRepos.save(mTicketSdeatils);
                if (files != null) {
                    for (MultipartFile file : files) {

                        Attachment attachment = new Attachment();
                        String originalFilename = file.getOriginalFilename();
                        String modifiedFileName = modifyFileName(originalFilename, save.getTicketcode());
                        attachment.setId(attachmentMasteRepo.newIdAttached());
                        attachment.setFileName(modifiedFileName);
                        attachment.setFileType(file.getContentType());
                        attachment.setTicketDt(save);

                        try {
                            // Save file on disk and get the path
                            String filePath1 = saveModifiedFile(file,modifiedFileName);
                            attachment.setFilePath(filePath1);
                            attachmentMasteRepo.save(attachment);

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }



                mTicketSdeatils getticketbyid = getticketbyid(save.getTicketid());

                if(getticketbyid.getEmployeeId()==null){
                    List<mEmployeeMaster> emplistByModName = moduleServices.getEmplistByModName(getticketbyid.getModulesid().getModcode());
                    if(emplistByModName!=null) {
                        List<String> toEmails = new ArrayList<>();
                        for (mEmployeeMaster emp : emplistByModName) {
                            if (emp.getEmailId() != null) {
                                toEmails.add(emp.getEmailId());
                            }
                        }

                    Map<String, Object> model = Map.of(
                            "requestId", getticketbyid.getTicketcode(),
                            "requestCategory", getticketbyid.getTicketlevel().getGmDescription(),
                            "shortDescription", getticketbyid.getTicketnote(),
                            "DescriptionCmeID", getticketbyid.getCmexpertId().getCmeId(),
                            "DescriptioncmeEmail", getticketbyid.getCmexpertId().getCmeemailId(),
                            "DescriptioncmePhone", getticketbyid.getCmexpertId().getCmephoneNo(),
                            "DescriptionClientName", getticketbyid.getClientid().getClientName(),
                            "DescriptionDesg", getticketbyid.getCmexpertId().getCmeDesignation()



                    );

                    emailcontroller.sendAssignmentEmail(toEmails,model);
                    }


                }

                return getticketbyid;

            }else{
                return null;
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

    public List<mTicketSdeatils> getallTicket(HttpServletRequest request) {
        try{

            List<mTicketSdeatils> all = ticketDRepos.findAllByOrderByTicketidDesc();
            if(all!=null){
                List<mTicketSdeatils> pass= new ArrayList<>();
                for(mTicketSdeatils checkall: all)
                {
                    mTicketSdeatils passtkt = new mTicketSdeatils();
                    passtkt.setTicketid(checkall.getTicketid());
                    passtkt.setTicketcode(checkall.getTicketcode());

                    mGeneralMaster tickettype = checkall.getTickettype();
                    if(tickettype!=null){
                        mGeneralMaster passType = new mGeneralMaster();
                                        passType.setGmid(tickettype.getGmid());
                                        passType.setGmType(tickettype.getGmType());
                                        passType.setGmDescription(tickettype.getGmDescription());
                        passtkt.setTickettype(passType);

                    }

                    mGeneralMaster ticketlevel = checkall.getTicketlevel();
                    if(ticketlevel!=null){
                        mGeneralMaster passLevel = new mGeneralMaster();
                        passLevel.setGmid(ticketlevel.getGmid());
                        passLevel.setGmType(ticketlevel.getGmType());
                        passLevel.setGmDescription(ticketlevel.getGmDescription());
                        passtkt.setTicketlevel(passLevel);

                    }
                    mGeneralMaster priority = checkall.getPriority();
                    if(priority!=null){
                        mGeneralMaster passPriority = new mGeneralMaster();
                        passPriority.setGmid(priority.getGmid());
                        passPriority.setGmType(priority.getGmType());
                        passPriority.setGmDescription(priority.getGmDescription());
                        passtkt.setPriority(passPriority);

                    }
                    mGeneralMaster status = checkall.getStatus();
                    if(status!=null){
                        mGeneralMaster passStatus = new mGeneralMaster();
                        passStatus.setGmid(status.getGmid());
                        passStatus.setGmType(status.getGmType());
                        passStatus.setGmDescription(status.getGmDescription());
                        passtkt.setStatus(passStatus);
                    }
                    passtkt.setCompanyname(checkall.getCompanyname());
                    mClientMaster clientid = checkall.getClientid();
                        if(clientid!=null){
                            mClientMaster passClient = new mClientMaster();
                           passClient.setCompanyMaster(null);
                            passClient.setMClientCMEMasterList(null);
                            List<mClientSPOCMaster> mClientSPOCMasters = clientid.getMClientSPOCMasters();
                            if(mClientSPOCMasters!=null){
                                List<mClientSPOCMaster> newpassspocclient =new ArrayList<>();
                                for(mClientSPOCMaster checkclientspoc:mClientSPOCMasters) {
                                    checkclientspoc.setClientmasterId(null);
                                    newpassspocclient.add(checkclientspoc);

                                }
                                passClient.setMClientSPOCMasters(newpassspocclient);

                            }else{
                                passClient.setMClientSPOCMasters(null);
                            }
                            passClient.setClientId(clientid.getClientId());
                            passClient.setClientName(clientid.getClientName());
                            passClient.setClientCode(clientid.getClientCode());
                            passClient.setClientGroup(clientid.getClientGroup());
                            passClient.setClientModule(clientid.getClientModule());
                            passClient.setIsactive(clientid.getIsactive());
                            passClient.setInsertdtm(clientid.getInsertdtm());
                            passClient.setUpdatedtm(clientid.getUpdatedtm());




                            passtkt.setClientid(passClient);
                        }
                    passtkt.setTicketnote(checkall.getTicketnote());
                    passtkt.setTicketdesc(checkall.getTicketdesc());
                    mModulesMaster modulesid = checkall.getModulesid();
                    if(modulesid!=null){
                        mModulesMaster passmod= new mModulesMaster();
                        passmod.setModId(modulesid.getModId());
                        passmod.setModcode(modulesid.getModcode());
                        passmod.setIsactive(modulesid.getIsactive());
                        passmod.setCompanyMaster(null);
                        passmod.setEmployeeMasterList(null);
                        passmod.setMClientCMEMasterList(null);
                    passtkt.setModulesid(passmod);
                    }else{
                        passtkt.setModulesid(null);
                    }

                    mClientCMEMaster cmexpertId = checkall.getCmexpertId();
                    if(cmexpertId!=null){
                        mClientCMEMaster passcme= new mClientCMEMaster();
                        passcme.setCmeId(cmexpertId.getCmeId());
                        passcme.setCmeName(cmexpertId.getCmeName());
                        passcme.setCmeDesignation(cmexpertId.getCmeDesignation());
                        passcme.setCmeemailId(cmexpertId.getCmeemailId());
                        passcme.setCmephoneNo(cmexpertId.getCmephoneNo());
                        passcme.setIsActive(cmexpertId.getIsActive());
                        passcme.setInsertdtm(cmexpertId.getInsertdtm());
                        passcme.setUpdatedtm(cmexpertId.getUpdatedtm());
                        passcme.setClientMasterIdCme(null);
                        passcme.setCmemodulesMaster(null);
                    passtkt.setCmexpertId(passcme);
                    }else{
                        passtkt.setCmexpertId(null);
                    }

                    mEmployeeMaster employeeId = checkall.getEmployeeId();
                    if(employeeId!=null){
                        mEmployeeMaster passemp = new mEmployeeMaster();
                        passemp.setModulesMaster_id(null);
                        passemp.setRoleMaster_id(null);
                        passemp.setInsertDtm(null);
                        passemp.setUpdateDtm(null);
                        passemp.setIsActive(null);
                        passemp.setDateOfJoining(null);
                        passemp.setEmpId(employeeId.getEmpId());
                        passemp.setEmpCode(employeeId.getEmpCode());
                        passemp.setIsActive(employeeId.getIsActive());
                        passemp.setEmpName(employeeId.getEmpName());
                        passemp.setEmailId(employeeId.getEmailId());
                        passemp.setPhoneNo(employeeId.getPhoneNo());
                        passemp.setCompanyName(employeeId.getCompanyName());
                        passtkt.setEmployeeId(passemp);
                    }else{
                        passtkt.setEmployeeId(null);
                    }


                    List<Attachment> attachments1 = checkall.getAttachments();
                    if(attachments1!=null) {
                        List<Attachment> passattach = new ArrayList<>();
                        for (Attachment attachment : attachments1) {
                            attachment.setTicketDt(null);
                            attachment.setFilePath(request.getRequestURL().toString().replace("/getAllTkt", "/downloadAttachment/" + attachment.getId()));
                            passattach.add(attachment);
                        }
                        passtkt.setAttachments(passattach);
                    }


                    passtkt.setCreatedon(checkall.getCreatedon());
                    passtkt.setReportedon(checkall.getReportedon());
                    pass.add(passtkt);

                }

                return pass;
            }else{
                return null;
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public mTicketSdeatils getticketbyid(Long id) {
        try{

            mTicketSdeatils checkall = ticketDRepos.findById(id).orElse(null);
            if(checkall!=null){


                    mTicketSdeatils passtkt = new mTicketSdeatils();
                    passtkt.setTicketid(checkall.getTicketid());
                    passtkt.setTicketcode(checkall.getTicketcode());

                    mGeneralMaster tickettype = checkall.getTickettype();
                    if(tickettype!=null){
                        mGeneralMaster passType = new mGeneralMaster();
                        passType.setGmid(tickettype.getGmid());
                        passType.setGmType(tickettype.getGmType());
                        passType.setGmDescription(tickettype.getGmDescription());
                        passtkt.setTickettype(passType);

                    }

                    mGeneralMaster ticketlevel = checkall.getTicketlevel();
                    if(ticketlevel!=null){
                        mGeneralMaster passLevel = new mGeneralMaster();
                        passLevel.setGmid(ticketlevel.getGmid());
                        passLevel.setGmType(ticketlevel.getGmType());
                        passLevel.setGmDescription(ticketlevel.getGmDescription());
                        passtkt.setTicketlevel(passLevel);

                    }
                    mGeneralMaster priority = checkall.getPriority();
                    if(priority!=null){
                        mGeneralMaster passPriority = new mGeneralMaster();
                        passPriority.setGmid(priority.getGmid());
                        passPriority.setGmType(priority.getGmType());
                        passPriority.setGmDescription(priority.getGmDescription());
                        passtkt.setPriority(passPriority);

                    }
                    mGeneralMaster status = checkall.getStatus();
                    if(status!=null){
                        mGeneralMaster passStatus = new mGeneralMaster();
                        passStatus.setGmid(status.getGmid());
                        passStatus.setGmType(status.getGmType());
                        passStatus.setGmDescription(status.getGmDescription());
                        passtkt.setStatus(passStatus);
                    }
                    passtkt.setCompanyname(checkall.getCompanyname());
                    mClientMaster clientid = checkall.getClientid();
                    if(clientid!=null){
                        mClientMaster passClient = new mClientMaster();
                        passClient.setCompanyMaster(null);
                        passClient.setMClientCMEMasterList(null);
                        List<mClientSPOCMaster> mClientSPOCMasters = clientid.getMClientSPOCMasters();
                        if(mClientSPOCMasters!=null){
                            List<mClientSPOCMaster> newpassspocclient =new ArrayList<>();
                            for(mClientSPOCMaster checkclientspoc:mClientSPOCMasters) {
                                checkclientspoc.setClientmasterId(null);
                                newpassspocclient.add(checkclientspoc);

                            }
                            passClient.setMClientSPOCMasters(newpassspocclient);

                        }else{
                            passClient.setMClientSPOCMasters(null);
                        }
                        passClient.setClientId(clientid.getClientId());
                        passClient.setClientName(clientid.getClientName());
                        passClient.setClientCode(clientid.getClientCode());
                        passClient.setClientGroup(clientid.getClientGroup());
                        passClient.setClientModule(clientid.getClientModule());
                        passClient.setIsactive(clientid.getIsactive());
                        passClient.setInsertdtm(clientid.getInsertdtm());
                        passClient.setUpdatedtm(clientid.getUpdatedtm());




                        passtkt.setClientid(passClient);
                    }
                    passtkt.setTicketnote(checkall.getTicketnote());
                    passtkt.setTicketdesc(checkall.getTicketdesc());
                    mModulesMaster modulesid = checkall.getModulesid();
                    if(modulesid!=null){
                        mModulesMaster passmod= new mModulesMaster();
                        passmod.setModId(modulesid.getModId());
                        passmod.setModcode(modulesid.getModcode());
                        passmod.setIsactive(modulesid.getIsactive());
                        passmod.setCompanyMaster(null);
                        passmod.setEmployeeMasterList(null);
                        passmod.setMClientCMEMasterList(null);
                        passtkt.setModulesid(passmod);
                    }else{
                        passtkt.setModulesid(null);
                    }

                    mClientCMEMaster cmexpertId = checkall.getCmexpertId();
                    if(cmexpertId!=null){
                        mClientCMEMaster passcme= new mClientCMEMaster();
                        passcme.setCmeId(cmexpertId.getCmeId());
                        passcme.setCmeName(cmexpertId.getCmeName());
                        passcme.setCmeDesignation(cmexpertId.getCmeDesignation());
                        passcme.setCmeemailId(cmexpertId.getCmeemailId());
                        passcme.setCmephoneNo(cmexpertId.getCmephoneNo());
                        passcme.setIsActive(cmexpertId.getIsActive());
                        passcme.setInsertdtm(cmexpertId.getInsertdtm());
                        passcme.setUpdatedtm(cmexpertId.getUpdatedtm());
                        passcme.setClientMasterIdCme(null);
                        passcme.setCmemodulesMaster(null);
                        passtkt.setCmexpertId(passcme);
                    }else{
                        passtkt.setCmexpertId(null);
                    }

                    mEmployeeMaster employeeId = checkall.getEmployeeId();
                    if(employeeId!=null){
                        mEmployeeMaster passemp = new mEmployeeMaster();
                        passemp.setModulesMaster_id(null);
                        passemp.setRoleMaster_id(null);
                        passemp.setInsertDtm(null);
                        passemp.setUpdateDtm(null);
                        passemp.setIsActive(null);
                        passemp.setDateOfJoining(null);
                        passemp.setEmpId(employeeId.getEmpId());
                        passemp.setEmpCode(employeeId.getEmpCode());
                        passemp.setIsActive(employeeId.getIsActive());
                        passemp.setEmpName(employeeId.getEmpName());
                        passemp.setEmailId(employeeId.getEmailId());
                        passemp.setPhoneNo(employeeId.getPhoneNo());
                        passemp.setCompanyName(employeeId.getCompanyName());
                        passtkt.setEmployeeId(passemp);
                    }else{
                        passtkt.setEmployeeId(null);
                    }

                List<Attachment> attachments1 = checkall.getAttachments();
                if(attachments1!=null){

                    List<Attachment> passattach = new ArrayList<>();
                    for (Attachment attachment : attachments1) {
                        attachment.setTicketDt(null);
                        passattach.add(attachment);
                    }
                    passtkt.setAttachments(passattach);

                }else{
                    passtkt.setAttachments(null);
                }
                    passtkt.setCreatedon(checkall.getCreatedon());
                    passtkt.setReportedon(checkall.getReportedon());

                return passtkt;
            }else{
                return null;
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public Integer gettotalTicket(){
        try{

            Integer totalTicketDetailsCount = ticketDRepos.getTotalTicketDetailsCount();
            return totalTicketDetailsCount;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
}



public Integer gettotalTicketByCmeID(Long cmeID){
        try{

            Integer byCmexpertId = ticketDRepos.findBycmexpertId(cmeID);
            return byCmexpertId;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
}


public List<mTicketSdeatils> getTicketofCmeID(Long cmeID,HttpServletRequest request){
        try{





                List<mTicketSdeatils> all = ticketDRepos.findListBycmexpertId(cmeID);
                if(all!=null){
                    List<mTicketSdeatils> pass= new ArrayList<>();
                    for(mTicketSdeatils checkall: all)
                    {
                        mTicketSdeatils passtkt = new mTicketSdeatils();
                        passtkt.setTicketid(checkall.getTicketid());
                        passtkt.setTicketcode(checkall.getTicketcode());

                        mGeneralMaster tickettype = checkall.getTickettype();
                        if(tickettype!=null){
                            mGeneralMaster passType = new mGeneralMaster();
                            passType.setGmid(tickettype.getGmid());
                            passType.setGmType(tickettype.getGmType());
                            passType.setGmDescription(tickettype.getGmDescription());
                            passtkt.setTickettype(passType);

                        }

                        mGeneralMaster ticketlevel = checkall.getTicketlevel();
                        if(ticketlevel!=null){
                            mGeneralMaster passLevel = new mGeneralMaster();
                            passLevel.setGmid(ticketlevel.getGmid());
                            passLevel.setGmType(ticketlevel.getGmType());
                            passLevel.setGmDescription(ticketlevel.getGmDescription());
                            passtkt.setTicketlevel(passLevel);

                        }
                        mGeneralMaster priority = checkall.getPriority();
                        if(priority!=null){
                            mGeneralMaster passPriority = new mGeneralMaster();
                            passPriority.setGmid(priority.getGmid());
                            passPriority.setGmType(priority.getGmType());
                            passPriority.setGmDescription(priority.getGmDescription());
                            passtkt.setPriority(passPriority);

                        }
                        mGeneralMaster status = checkall.getStatus();
                        if(status!=null){
                            mGeneralMaster passStatus = new mGeneralMaster();
                            passStatus.setGmid(status.getGmid());
                            passStatus.setGmType(status.getGmType());
                            passStatus.setGmDescription(status.getGmDescription());
                            passtkt.setStatus(passStatus);
                        }
                        passtkt.setCompanyname(checkall.getCompanyname());
                        mClientMaster clientid = checkall.getClientid();
                        if(clientid!=null){
                            mClientMaster passClient = new mClientMaster();
                            passClient.setCompanyMaster(null);
                            passClient.setMClientCMEMasterList(null);
                            List<mClientSPOCMaster> mClientSPOCMasters = clientid.getMClientSPOCMasters();
                            if(mClientSPOCMasters!=null){
                                List<mClientSPOCMaster> newpassspocclient =new ArrayList<>();
                                for(mClientSPOCMaster checkclientspoc:mClientSPOCMasters) {
                                    checkclientspoc.setClientmasterId(null);
                                    newpassspocclient.add(checkclientspoc);

                                }
                                passClient.setMClientSPOCMasters(newpassspocclient);

                            }else{
                                passClient.setMClientSPOCMasters(null);
                            }
                            passClient.setClientId(clientid.getClientId());
                            passClient.setClientName(clientid.getClientName());
                            passClient.setClientCode(clientid.getClientCode());
                            passClient.setClientGroup(clientid.getClientGroup());
                            passClient.setClientModule(clientid.getClientModule());
                            passClient.setIsactive(clientid.getIsactive());
                            passClient.setInsertdtm(clientid.getInsertdtm());
                            passClient.setUpdatedtm(clientid.getUpdatedtm());




                            passtkt.setClientid(passClient);
                        }
                        passtkt.setTicketnote(checkall.getTicketnote());
                        passtkt.setTicketdesc(checkall.getTicketdesc());
                        mModulesMaster modulesid = checkall.getModulesid();
                        if(modulesid!=null){
                            mModulesMaster passmod= new mModulesMaster();
                            passmod.setModId(modulesid.getModId());
                            passmod.setModcode(modulesid.getModcode());
                            passmod.setIsactive(modulesid.getIsactive());
                            passmod.setCompanyMaster(null);
                            passmod.setEmployeeMasterList(null);
                            passmod.setMClientCMEMasterList(null);
                            passtkt.setModulesid(passmod);
                        }else{
                            passtkt.setModulesid(null);
                        }

                        mClientCMEMaster cmexpertId = checkall.getCmexpertId();
                        if(cmexpertId!=null){
                            mClientCMEMaster passcme= new mClientCMEMaster();
                            passcme.setCmeId(cmexpertId.getCmeId());
                            passcme.setCmeName(cmexpertId.getCmeName());
                            passcme.setCmeDesignation(cmexpertId.getCmeDesignation());
                            passcme.setCmeemailId(cmexpertId.getCmeemailId());
                            passcme.setCmephoneNo(cmexpertId.getCmephoneNo());
                            passcme.setIsActive(cmexpertId.getIsActive());
                            passcme.setInsertdtm(cmexpertId.getInsertdtm());
                            passcme.setUpdatedtm(cmexpertId.getUpdatedtm());
                            passcme.setClientMasterIdCme(null);
                            passcme.setCmemodulesMaster(null);
                            passtkt.setCmexpertId(passcme);
                        }else{
                            passtkt.setCmexpertId(null);
                        }

                        mEmployeeMaster employeeId = checkall.getEmployeeId();
                        if(employeeId!=null){
                            mEmployeeMaster passemp = new mEmployeeMaster();
                            passemp.setModulesMaster_id(null);
                            passemp.setRoleMaster_id(null);
                            passemp.setInsertDtm(null);
                            passemp.setUpdateDtm(null);
                            passemp.setIsActive(null);
                            passemp.setDateOfJoining(null);
                            passemp.setEmpId(employeeId.getEmpId());
                            passemp.setEmpCode(employeeId.getEmpCode());
                            passemp.setIsActive(employeeId.getIsActive());
                            passemp.setEmpName(employeeId.getEmpName());
                            passemp.setEmailId(employeeId.getEmailId());
                            passemp.setPhoneNo(employeeId.getPhoneNo());
                            passemp.setCompanyName(employeeId.getCompanyName());
                            passtkt.setEmployeeId(passemp);
                        }else{
                            passtkt.setEmployeeId(null);
                        }


                        List<Attachment> attachments1 = passtkt.getAttachments();
                        if(attachments1!=null){
                            List<Attachment> passattach = new ArrayList<>();
                            for(Attachment attachment: attachments1){
                                attachment.setTicketDt(null);
                                attachment.setFilePath(request.getRequestURL().toString().replace("/getTicketWithAttachments/" + cmeID, "/downloadAttachment/" + attachment.getId()));
                                passattach.add(attachment);
                            }
                            passtkt.setAttachments(passattach);

                        }else{
                            passtkt.setAttachments(null);
                        }


                        passtkt.setCreatedon(checkall.getCreatedon());
                        passtkt.setReportedon(checkall.getReportedon());
                        pass.add(passtkt);

                    }

                    return pass;
                }else{
                    return null;
                }


            } catch (Exception e) {
                throw new RuntimeException(e);
            }



























}






    public List<mGeneralMaster> dropdownOFType(){
        try{
            List<mGeneralMaster> findByname = masterServices.getFindByname("TT");
            if(findByname!=null){
                return findByname;
            }else{
                return null;
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<mGeneralMaster> dropdownOFLevel(){
        try{
            List<mGeneralMaster> findByname = masterServices.getFindByname("TL");
            if(findByname!=null){
                return findByname;
            }else{
                return null;
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<mGeneralMaster> dropdownOFPriority(){
        try{
            List<mGeneralMaster> findByname = masterServices.getFindByname("TP");
            if(findByname!=null){
                return findByname;
            }else{
                return null;
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<mGeneralMaster> dropdownOFStatus(){
        try{
            List<mGeneralMaster> findByname = masterServices.getFindByname("TS");
            if(findByname!=null){
                List<mGeneralMaster> passStatus=new ArrayList<>();
                for(mGeneralMaster passgmSt : findByname){
                    if(!passgmSt.getGmDescription().equals("NEW")|| !passgmSt.getGmDescription().equals("CANCEL")||!passgmSt.getGmDescription().equals("CLOSED")){
                        passStatus.add(passgmSt);

                    }

                }
                return passStatus;
            }else{
                return null;
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<mGeneralMaster> dropdownOFStatusEMP(){
        try{
            List<mGeneralMaster> findByname = masterServices.getFindByname("TS");
            if(findByname!=null){
                List<mGeneralMaster> passStatus=new ArrayList<>();
                for(mGeneralMaster passgmSt : findByname){
                    if(!passgmSt.getGmDescription().equals("NEW") && !passgmSt.getGmDescription().equals("CANCEL")&& !passgmSt.getGmDescription().equals("CLOSED")){
                        passStatus.add(passgmSt);

                    }

                }
                return passStatus;
            }else{
                return null;
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<mGeneralMaster> dropdownOFStatusClient(){
        try{
            List<mGeneralMaster> findByname = masterServices.getFindByname("TS");
            if(findByname!=null){
                List<mGeneralMaster> passStatus=new ArrayList<>();
                for(mGeneralMaster passgmSt : findByname){
                    if(passgmSt.getGmDescription().equals("IN-PROGRESS") ||passgmSt.getGmDescription().equals("PENDING") || passgmSt.getGmDescription().equals("CANCEL")|| passgmSt.getGmDescription().equals("CLOSED")){
                        passStatus.add(passgmSt);

                    }

                }
                return passStatus;
            }else{
                return null;
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//Manager total ticket
    public  List<mTicketSdeatils> getAllTicketMgr(String cmpName,HttpServletRequest request){
        try{

            List<mTicketSdeatils> all = ticketDRepos.findByCompanyname(cmpName);

            if(all!=null){
                List<mTicketSdeatils> pass= new ArrayList<>();
                for(mTicketSdeatils checkall: all)
                {
                    mTicketSdeatils passtkt = new mTicketSdeatils();
                    passtkt.setTicketid(checkall.getTicketid());
                    passtkt.setTicketcode(checkall.getTicketcode());

                    mGeneralMaster tickettype = checkall.getTickettype();
                    if(tickettype!=null){
                        mGeneralMaster passType = new mGeneralMaster();
                        passType.setGmid(tickettype.getGmid());
                        passType.setGmType(tickettype.getGmType());
                        passType.setGmDescription(tickettype.getGmDescription());
                        passtkt.setTickettype(passType);

                    }

                    mGeneralMaster ticketlevel = checkall.getTicketlevel();
                    if(ticketlevel!=null){
                        mGeneralMaster passLevel = new mGeneralMaster();
                        passLevel.setGmid(ticketlevel.getGmid());
                        passLevel.setGmType(ticketlevel.getGmType());
                        passLevel.setGmDescription(ticketlevel.getGmDescription());
                        passtkt.setTicketlevel(passLevel);

                    }
                    mGeneralMaster priority = checkall.getPriority();
                    if(priority!=null){
                        mGeneralMaster passPriority = new mGeneralMaster();
                        passPriority.setGmid(priority.getGmid());
                        passPriority.setGmType(priority.getGmType());
                        passPriority.setGmDescription(priority.getGmDescription());
                        passtkt.setPriority(passPriority);

                    }
                    mGeneralMaster status = checkall.getStatus();
                    if(status!=null){
                        mGeneralMaster passStatus = new mGeneralMaster();
                        passStatus.setGmid(status.getGmid());
                        passStatus.setGmType(status.getGmType());
                        passStatus.setGmDescription(status.getGmDescription());
                        passtkt.setStatus(passStatus);
                    }
                    passtkt.setCompanyname(checkall.getCompanyname());
                    mClientMaster clientid = checkall.getClientid();
                    if(clientid!=null){
                        mClientMaster passClient = new mClientMaster();
                        passClient.setCompanyMaster(null);
                        passClient.setMClientCMEMasterList(null);
                        List<mClientSPOCMaster> mClientSPOCMasters = clientid.getMClientSPOCMasters();
                        if(mClientSPOCMasters!=null){
                            List<mClientSPOCMaster> newpassspocclient =new ArrayList<>();
                            for(mClientSPOCMaster checkclientspoc:mClientSPOCMasters) {
                                checkclientspoc.setClientmasterId(null);
                                newpassspocclient.add(checkclientspoc);

                            }
                            passClient.setMClientSPOCMasters(newpassspocclient);

                        }else{
                            passClient.setMClientSPOCMasters(null);
                        }
                        passClient.setClientId(clientid.getClientId());
                        passClient.setClientName(clientid.getClientName());
                        passClient.setClientCode(clientid.getClientCode());
                        passClient.setClientGroup(clientid.getClientGroup());
                        passClient.setClientModule(clientid.getClientModule());
                        passClient.setIsactive(clientid.getIsactive());
                        passClient.setInsertdtm(clientid.getInsertdtm());
                        passClient.setUpdatedtm(clientid.getUpdatedtm());




                        passtkt.setClientid(passClient);
                    }
                    passtkt.setTicketnote(checkall.getTicketnote());
                    passtkt.setTicketdesc(checkall.getTicketdesc());
                    mModulesMaster modulesid = checkall.getModulesid();
                    if(modulesid!=null){
                        mModulesMaster passmod= new mModulesMaster();
                        passmod.setModId(modulesid.getModId());
                        passmod.setModcode(modulesid.getModcode());
                        passmod.setIsactive(modulesid.getIsactive());
                        passmod.setCompanyMaster(null);
                        passmod.setEmployeeMasterList(null);
                        passmod.setMClientCMEMasterList(null);
                        passtkt.setModulesid(passmod);
                    }else{
                        passtkt.setModulesid(null);
                    }

                    mClientCMEMaster cmexpertId = checkall.getCmexpertId();
                    if(cmexpertId!=null){
                        mClientCMEMaster passcme= new mClientCMEMaster();
                        passcme.setCmeId(cmexpertId.getCmeId());
                        passcme.setCmeName(cmexpertId.getCmeName());
                        passcme.setCmeDesignation(cmexpertId.getCmeDesignation());
                        passcme.setCmeemailId(cmexpertId.getCmeemailId());
                        passcme.setCmephoneNo(cmexpertId.getCmephoneNo());
                        passcme.setIsActive(cmexpertId.getIsActive());
                        passcme.setInsertdtm(cmexpertId.getInsertdtm());
                        passcme.setUpdatedtm(cmexpertId.getUpdatedtm());
                        passcme.setClientMasterIdCme(null);
                        passcme.setCmemodulesMaster(null);
                        passtkt.setCmexpertId(passcme);
                    }else{
                        passtkt.setCmexpertId(null);
                    }

                    mEmployeeMaster employeeId = checkall.getEmployeeId();
                    if(employeeId!=null){
                        mEmployeeMaster passemp = new mEmployeeMaster();
                        passemp.setModulesMaster_id(null);
                        passemp.setRoleMaster_id(null);
                        passemp.setInsertDtm(null);
                        passemp.setUpdateDtm(null);
                        passemp.setIsActive(null);
                        passemp.setDateOfJoining(null);
                        passemp.setEmpId(employeeId.getEmpId());
                        passemp.setEmpCode(employeeId.getEmpCode());
                        passemp.setIsActive(employeeId.getIsActive());
                        passemp.setEmpName(employeeId.getEmpName());
                        passemp.setEmailId(employeeId.getEmailId());
                        passemp.setPhoneNo(employeeId.getPhoneNo());
                        passemp.setCompanyName(employeeId.getCompanyName());
                        passtkt.setEmployeeId(passemp);
                    }else{
                        passtkt.setEmployeeId(null);
                    }

                    List<Attachment> attachments = checkall.getAttachments();
                    if(attachments!=null){
                        List<Attachment> passattach = new ArrayList<>();
                        for(Attachment attachment: attachments){
                            attachment.setTicketDt(null);
                            passattach.add(attachment);
                        }
                        passtkt.setAttachments(passattach);

                    }else{
                        passtkt.setAttachments(null);
                    }
                    List<Attachment> attachments1 = checkall.getAttachments();
                    if(attachments1!=null) {
                        List<Attachment> passattach = new ArrayList<>();
                        for (Attachment attachment : attachments1) {
                            attachment.setTicketDt(null);
                            attachment.setFilePath(request.getRequestURL().toString().replace("/getAllTkt", "/downloadAttachment/" + attachment.getId()));
                            passattach.add(attachment);
                        }
                        passtkt.setAttachments(passattach);
                    }


                    passtkt.setCreatedon(checkall.getCreatedon());
                    passtkt.setReportedon(checkall.getReportedon());
                    pass.add(passtkt);

                }

                return pass;
            }else{
                return null;
            }



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long gettotalTicketByMGRCompany(String cmeID){
        try{

            Long byCmexpertId = ticketDRepos.countBycompanyname(cmeID);
            return byCmexpertId;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public mTicketSdeatils assignEmpByMGR(Long tcktID,Integer empID,HttpServletRequest request ){
        try{

            mEmployeeMaster employeeById = employeeService.getEmployeeById(empID);
            if(employeeById!=null) {
//                mTicketSdeatils getticketbyid = getticketbyid(tcktID, request);
                mTicketSdeatils getticketbyid = ticketDRepos.findById(tcktID).orElse(null);
                if (getticketbyid != null) {
                    getticketbyid.setEmployeeId(employeeById);
                    mTicketSdeatils checkall = ticketDRepos.save(getticketbyid);

                    if (checkall != null) {


                        mTicketSdeatils passtkt = new mTicketSdeatils();
                        passtkt.setTicketid(checkall.getTicketid());
                        passtkt.setTicketcode(checkall.getTicketcode());

                        mGeneralMaster tickettype = checkall.getTickettype();
                        if (tickettype != null) {
                            mGeneralMaster passType = new mGeneralMaster();
                            passType.setGmid(tickettype.getGmid());
                            passType.setGmType(tickettype.getGmType());
                            passType.setGmDescription(tickettype.getGmDescription());
                            passtkt.setTickettype(passType);

                        }

                        mGeneralMaster ticketlevel = checkall.getTicketlevel();
                        if (ticketlevel != null) {
                            mGeneralMaster passLevel = new mGeneralMaster();
                            passLevel.setGmid(ticketlevel.getGmid());
                            passLevel.setGmType(ticketlevel.getGmType());
                            passLevel.setGmDescription(ticketlevel.getGmDescription());
                            passtkt.setTicketlevel(passLevel);

                        }
                        mGeneralMaster priority = checkall.getPriority();
                        if (priority != null) {
                            mGeneralMaster passPriority = new mGeneralMaster();
                            passPriority.setGmid(priority.getGmid());
                            passPriority.setGmType(priority.getGmType());
                            passPriority.setGmDescription(priority.getGmDescription());
                            passtkt.setPriority(passPriority);

                        }
                        mGeneralMaster status = checkall.getStatus();
                        if (status != null) {
                            mGeneralMaster passStatus = new mGeneralMaster();
                            passStatus.setGmid(status.getGmid());
                            passStatus.setGmType(status.getGmType());
                            passStatus.setGmDescription(status.getGmDescription());
                            passtkt.setStatus(passStatus);
                        }
                        passtkt.setCompanyname(checkall.getCompanyname());
                        mClientMaster clientid = checkall.getClientid();
                        if (clientid != null) {
                            mClientMaster passClient = new mClientMaster();
                            passClient.setCompanyMaster(null);
                            passClient.setMClientCMEMasterList(null);
                            List<mClientSPOCMaster> mClientSPOCMasters = clientid.getMClientSPOCMasters();
                            if (mClientSPOCMasters != null) {
                                List<mClientSPOCMaster> newpassspocclient = new ArrayList<>();
                                for (mClientSPOCMaster checkclientspoc : mClientSPOCMasters) {
                                    checkclientspoc.setClientmasterId(null);
                                    newpassspocclient.add(checkclientspoc);

                                }
                                passClient.setMClientSPOCMasters(newpassspocclient);

                            } else {
                                passClient.setMClientSPOCMasters(null);
                            }
                            passClient.setClientId(clientid.getClientId());
                            passClient.setClientName(clientid.getClientName());
                            passClient.setClientCode(clientid.getClientCode());
                            passClient.setClientGroup(clientid.getClientGroup());
                            passClient.setClientModule(clientid.getClientModule());
                            passClient.setIsactive(clientid.getIsactive());
                            passClient.setInsertdtm(clientid.getInsertdtm());
                            passClient.setUpdatedtm(clientid.getUpdatedtm());


                            passtkt.setClientid(passClient);
                        }
                        passtkt.setTicketnote(checkall.getTicketnote());
                        passtkt.setTicketdesc(checkall.getTicketdesc());
                        mModulesMaster modulesid = checkall.getModulesid();
                        if (modulesid != null) {
                            mModulesMaster passmod = new mModulesMaster();
                            passmod.setModId(modulesid.getModId());
                            passmod.setModcode(modulesid.getModcode());
                            passmod.setIsactive(modulesid.getIsactive());
                            passmod.setCompanyMaster(null);
                            passmod.setEmployeeMasterList(null);
                            passmod.setMClientCMEMasterList(null);
                            passtkt.setModulesid(passmod);
                        } else {
                            passtkt.setModulesid(null);
                        }

                        mClientCMEMaster cmexpertId = checkall.getCmexpertId();
                        if (cmexpertId != null) {
                            mClientCMEMaster passcme = new mClientCMEMaster();
                            passcme.setCmeId(cmexpertId.getCmeId());
                            passcme.setCmeName(cmexpertId.getCmeName());
                            passcme.setCmeDesignation(cmexpertId.getCmeDesignation());
                            passcme.setCmeemailId(cmexpertId.getCmeemailId());
                            passcme.setCmephoneNo(cmexpertId.getCmephoneNo());
                            passcme.setIsActive(cmexpertId.getIsActive());
                            passcme.setInsertdtm(cmexpertId.getInsertdtm());
                            passcme.setUpdatedtm(cmexpertId.getUpdatedtm());
                            passcme.setClientMasterIdCme(null);
                            passcme.setCmemodulesMaster(null);
                            passtkt.setCmexpertId(passcme);
                        } else {
                            passtkt.setCmexpertId(null);
                        }

                        mEmployeeMaster employeeId = checkall.getEmployeeId();
                        if (employeeId != null) {
                            mEmployeeMaster passemp = new mEmployeeMaster();
                            passemp.setModulesMaster_id(null);
                            passemp.setRoleMaster_id(null);
                            passemp.setInsertDtm(null);
                            passemp.setUpdateDtm(null);
                            passemp.setIsActive(null);
                            passemp.setDateOfJoining(null);
                            passemp.setEmpId(employeeId.getEmpId());
                            passemp.setEmpCode(employeeId.getEmpCode());
                            passemp.setIsActive(employeeId.getIsActive());
                            passemp.setEmpName(employeeId.getEmpName());
                            passemp.setEmailId(employeeId.getEmailId());
                            passemp.setPhoneNo(employeeId.getPhoneNo());
                            passemp.setCompanyName(employeeId.getCompanyName());
                            passtkt.setEmployeeId(passemp);
                        } else {
                            passtkt.setEmployeeId(null);
                        }

                        List<Attachment> attachments1 = passtkt.getAttachments();
                        if (attachments1 != null) {
                            List<Attachment> passattach = new ArrayList<>();
                            for (Attachment attachment : attachments1) {
                                attachment.setTicketDt(null);
                                attachment.setFilePath(request.getRequestURL().toString().replace("/getTicketWithAttachments/" + tcktID, "/downloadAttachment/" + attachment.getId()));
                                passattach.add(attachment);
                            }
                            passtkt.setAttachments(passattach);

                        } else {
                            passtkt.setAttachments(null);
                        }
                        passtkt.setCreatedon(checkall.getCreatedon());
                        passtkt.setReportedon(checkall.getReportedon());

                        return passtkt;


                    } else {
                        throw new RuntimeException("not updated ticket");
                    }


                } else {
                    throw new RuntimeException("not found ticket");
                }

            }else {
                throw new RuntimeException("not found employee");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public mTicketSdeatils updateStatusByMGR(Long tcktID,Integer empID,HttpServletRequest request ){
        try{

            mGeneralMaster employeeById = masterServices.getbyidGeneralMaster(empID);

            if(employeeById!=null) {
//                mTicketSdeatils getticketbyid = getticketbyid(tcktID, request);


                mTicketSdeatils getticketbyid = ticketDRepos.findById(tcktID).orElse(null);
                if (getticketbyid != null) {
                    getticketbyid.setStatus(employeeById);


                    mTicketSdeatils checkall = ticketDRepos.save(getticketbyid);

                    if (checkall != null) {


                        mTicketSdeatils passtkt = new mTicketSdeatils();
                        passtkt.setTicketid(checkall.getTicketid());
                        passtkt.setTicketcode(checkall.getTicketcode());

                        mGeneralMaster tickettype = checkall.getTickettype();
                        if (tickettype != null) {
                            mGeneralMaster passType = new mGeneralMaster();
                            passType.setGmid(tickettype.getGmid());
                            passType.setGmType(tickettype.getGmType());
                            passType.setGmDescription(tickettype.getGmDescription());
                            passtkt.setTickettype(passType);

                        }

                        mGeneralMaster ticketlevel = checkall.getTicketlevel();
                        if (ticketlevel != null) {
                            mGeneralMaster passLevel = new mGeneralMaster();
                            passLevel.setGmid(ticketlevel.getGmid());
                            passLevel.setGmType(ticketlevel.getGmType());
                            passLevel.setGmDescription(ticketlevel.getGmDescription());
                            passtkt.setTicketlevel(passLevel);

                        }
                        mGeneralMaster priority = checkall.getPriority();
                        if (priority != null) {
                            mGeneralMaster passPriority = new mGeneralMaster();
                            passPriority.setGmid(priority.getGmid());
                            passPriority.setGmType(priority.getGmType());
                            passPriority.setGmDescription(priority.getGmDescription());
                            passtkt.setPriority(passPriority);

                        }
                        mGeneralMaster status = checkall.getStatus();
                        if (status != null) {
                            mGeneralMaster passStatus = new mGeneralMaster();
                            passStatus.setGmid(status.getGmid());
                            passStatus.setGmType(status.getGmType());
                            passStatus.setGmDescription(status.getGmDescription());
                            passtkt.setStatus(passStatus);
                        }
                        passtkt.setCompanyname(checkall.getCompanyname());
                        mClientMaster clientid = checkall.getClientid();
                        if (clientid != null) {
                            mClientMaster passClient = new mClientMaster();
                            passClient.setCompanyMaster(null);
                            passClient.setMClientCMEMasterList(null);
                            List<mClientSPOCMaster> mClientSPOCMasters = clientid.getMClientSPOCMasters();
                            if (mClientSPOCMasters != null) {
                                List<mClientSPOCMaster> newpassspocclient = new ArrayList<>();
                                for (mClientSPOCMaster checkclientspoc : mClientSPOCMasters) {
                                    checkclientspoc.setClientmasterId(null);
                                    newpassspocclient.add(checkclientspoc);

                                }
                                passClient.setMClientSPOCMasters(newpassspocclient);

                            } else {
                                passClient.setMClientSPOCMasters(null);
                            }
                            passClient.setClientId(clientid.getClientId());
                            passClient.setClientName(clientid.getClientName());
                            passClient.setClientCode(clientid.getClientCode());
                            passClient.setClientGroup(clientid.getClientGroup());
                            passClient.setClientModule(clientid.getClientModule());
                            passClient.setIsactive(clientid.getIsactive());
                            passClient.setInsertdtm(clientid.getInsertdtm());
                            passClient.setUpdatedtm(clientid.getUpdatedtm());


                            passtkt.setClientid(passClient);
                        }
                        passtkt.setTicketnote(checkall.getTicketnote());
                        passtkt.setTicketdesc(checkall.getTicketdesc());
                        mModulesMaster modulesid = checkall.getModulesid();
                        if (modulesid != null) {
                            mModulesMaster passmod = new mModulesMaster();
                            passmod.setModId(modulesid.getModId());
                            passmod.setModcode(modulesid.getModcode());
                            passmod.setIsactive(modulesid.getIsactive());
                            passmod.setCompanyMaster(null);
                            passmod.setEmployeeMasterList(null);
                            passmod.setMClientCMEMasterList(null);
                            passtkt.setModulesid(passmod);
                        } else {
                            passtkt.setModulesid(null);
                        }

                        mClientCMEMaster cmexpertId = checkall.getCmexpertId();
                        if (cmexpertId != null) {
                            mClientCMEMaster passcme = new mClientCMEMaster();
                            passcme.setCmeId(cmexpertId.getCmeId());
                            passcme.setCmeName(cmexpertId.getCmeName());
                            passcme.setCmeDesignation(cmexpertId.getCmeDesignation());
                            passcme.setCmeemailId(cmexpertId.getCmeemailId());
                            passcme.setCmephoneNo(cmexpertId.getCmephoneNo());
                            passcme.setIsActive(cmexpertId.getIsActive());
                            passcme.setInsertdtm(cmexpertId.getInsertdtm());
                            passcme.setUpdatedtm(cmexpertId.getUpdatedtm());
                            passcme.setClientMasterIdCme(null);
                            passcme.setCmemodulesMaster(null);
                            passtkt.setCmexpertId(passcme);
                        } else {
                            passtkt.setCmexpertId(null);
                        }

                        mEmployeeMaster employeeId = checkall.getEmployeeId();
                        if (employeeId != null) {
                            mEmployeeMaster passemp = new mEmployeeMaster();
                            passemp.setModulesMaster_id(null);
                            passemp.setRoleMaster_id(null);
                            passemp.setInsertDtm(null);
                            passemp.setUpdateDtm(null);
                            passemp.setIsActive(null);
                            passemp.setDateOfJoining(null);
                            passemp.setEmpId(employeeId.getEmpId());
                            passemp.setEmpCode(employeeId.getEmpCode());
                            passemp.setIsActive(employeeId.getIsActive());
                            passemp.setEmpName(employeeId.getEmpName());
                            passemp.setEmailId(employeeId.getEmailId());
                            passemp.setPhoneNo(employeeId.getPhoneNo());
                            passemp.setCompanyName(employeeId.getCompanyName());
                            passtkt.setEmployeeId(passemp);
                        } else {
                            passtkt.setEmployeeId(null);
                        }

                        List<Attachment> attachments1 = passtkt.getAttachments();
                        if (attachments1 != null) {
                            List<Attachment> passattach = new ArrayList<>();
                            for (Attachment attachment : attachments1) {
                                attachment.setTicketDt(null);
                                attachment.setFilePath(request.getRequestURL().toString().replace("/getTicketWithAttachments/" + tcktID, "/downloadAttachment/" + attachment.getId()));
                                passattach.add(attachment);
                            }
                            passtkt.setAttachments(passattach);

                        } else {
                            passtkt.setAttachments(null);
                        }
                        passtkt.setCreatedon(checkall.getCreatedon());
                        passtkt.setReportedon(checkall.getReportedon());

                        return passtkt;


                    } else {
                        throw new RuntimeException("not updated ticket");
                    }


                } else {
                    throw new RuntimeException("not found ticket");
                }

            }else {
                throw new RuntimeException("not found employee");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public  List<mTicketSdeatils> getAllTicketEmp(Long employeesId,HttpServletRequest request){
        try{

            List<mTicketSdeatils> all = ticketDRepos.findByemployeeId(employeesId);

            if(all!=null){
                List<mTicketSdeatils> pass= new ArrayList<>();
                for(mTicketSdeatils checkall: all)
                {
                    mTicketSdeatils passtkt = new mTicketSdeatils();
                    passtkt.setTicketid(checkall.getTicketid());
                    passtkt.setTicketcode(checkall.getTicketcode());

                    mGeneralMaster tickettype = checkall.getTickettype();
                    if(tickettype!=null){
                        mGeneralMaster passType = new mGeneralMaster();
                        passType.setGmid(tickettype.getGmid());
                        passType.setGmType(tickettype.getGmType());
                        passType.setGmDescription(tickettype.getGmDescription());
                        passtkt.setTickettype(passType);

                    }

                    mGeneralMaster ticketlevel = checkall.getTicketlevel();
                    if(ticketlevel!=null){
                        mGeneralMaster passLevel = new mGeneralMaster();
                        passLevel.setGmid(ticketlevel.getGmid());
                        passLevel.setGmType(ticketlevel.getGmType());
                        passLevel.setGmDescription(ticketlevel.getGmDescription());
                        passtkt.setTicketlevel(passLevel);

                    }
                    mGeneralMaster priority = checkall.getPriority();
                    if(priority!=null){
                        mGeneralMaster passPriority = new mGeneralMaster();
                        passPriority.setGmid(priority.getGmid());
                        passPriority.setGmType(priority.getGmType());
                        passPriority.setGmDescription(priority.getGmDescription());
                        passtkt.setPriority(passPriority);

                    }
                    mGeneralMaster status = checkall.getStatus();
                    if(status!=null){
                        mGeneralMaster passStatus = new mGeneralMaster();
                        passStatus.setGmid(status.getGmid());
                        passStatus.setGmType(status.getGmType());
                        passStatus.setGmDescription(status.getGmDescription());
                        passtkt.setStatus(passStatus);
                    }
                    passtkt.setCompanyname(checkall.getCompanyname());
                    mClientMaster clientid = checkall.getClientid();
                    if(clientid!=null){
                        mClientMaster passClient = new mClientMaster();
                        passClient.setCompanyMaster(null);
                        passClient.setMClientCMEMasterList(null);
                        List<mClientSPOCMaster> mClientSPOCMasters = clientid.getMClientSPOCMasters();
                        if(mClientSPOCMasters!=null){
                            List<mClientSPOCMaster> newpassspocclient =new ArrayList<>();
                            for(mClientSPOCMaster checkclientspoc:mClientSPOCMasters) {
                                checkclientspoc.setClientmasterId(null);
                                newpassspocclient.add(checkclientspoc);

                            }
                            passClient.setMClientSPOCMasters(newpassspocclient);

                        }else{
                            passClient.setMClientSPOCMasters(null);
                        }
                        passClient.setClientId(clientid.getClientId());
                        passClient.setClientName(clientid.getClientName());
                        passClient.setClientCode(clientid.getClientCode());
                        passClient.setClientGroup(clientid.getClientGroup());
                        passClient.setClientModule(clientid.getClientModule());
                        passClient.setIsactive(clientid.getIsactive());
                        passClient.setInsertdtm(clientid.getInsertdtm());
                        passClient.setUpdatedtm(clientid.getUpdatedtm());




                        passtkt.setClientid(passClient);
                    }
                    passtkt.setTicketnote(checkall.getTicketnote());
                    passtkt.setTicketdesc(checkall.getTicketdesc());
                    mModulesMaster modulesid = checkall.getModulesid();
                    if(modulesid!=null){
                        mModulesMaster passmod= new mModulesMaster();
                        passmod.setModId(modulesid.getModId());
                        passmod.setModcode(modulesid.getModcode());
                        passmod.setIsactive(modulesid.getIsactive());
                        passmod.setCompanyMaster(null);
                        passmod.setEmployeeMasterList(null);
                        passmod.setMClientCMEMasterList(null);
                        passtkt.setModulesid(passmod);
                    }else{
                        passtkt.setModulesid(null);
                    }

                    mClientCMEMaster cmexpertId = checkall.getCmexpertId();
                    if(cmexpertId!=null){
                        mClientCMEMaster passcme= new mClientCMEMaster();
                        passcme.setCmeId(cmexpertId.getCmeId());
                        passcme.setCmeName(cmexpertId.getCmeName());
                        passcme.setCmeDesignation(cmexpertId.getCmeDesignation());
                        passcme.setCmeemailId(cmexpertId.getCmeemailId());
                        passcme.setCmephoneNo(cmexpertId.getCmephoneNo());
                        passcme.setIsActive(cmexpertId.getIsActive());
                        passcme.setInsertdtm(cmexpertId.getInsertdtm());
                        passcme.setUpdatedtm(cmexpertId.getUpdatedtm());
                        passcme.setClientMasterIdCme(null);
                        passcme.setCmemodulesMaster(null);
                        passtkt.setCmexpertId(passcme);
                    }else{
                        passtkt.setCmexpertId(null);
                    }

                    mEmployeeMaster employeeId = checkall.getEmployeeId();
                    if(employeeId!=null){
                        mEmployeeMaster passemp = new mEmployeeMaster();
                        passemp.setModulesMaster_id(null);
                        passemp.setRoleMaster_id(null);
                        passemp.setInsertDtm(null);
                        passemp.setUpdateDtm(null);
                        passemp.setIsActive(null);
                        passemp.setDateOfJoining(null);
                        passemp.setEmpId(employeeId.getEmpId());
                        passemp.setEmpCode(employeeId.getEmpCode());
                        passemp.setIsActive(employeeId.getIsActive());
                        passemp.setEmpName(employeeId.getEmpName());
                        passemp.setEmailId(employeeId.getEmailId());
                        passemp.setPhoneNo(employeeId.getPhoneNo());
                        passemp.setCompanyName(employeeId.getCompanyName());
                        passtkt.setEmployeeId(passemp);
                    }else{
                        passtkt.setEmployeeId(null);
                    }

                    List<Attachment> attachments = checkall.getAttachments();
                    if(attachments!=null){
                        List<Attachment> passattach = new ArrayList<>();
                        for(Attachment attachment: attachments){
                            attachment.setTicketDt(null);
                            passattach.add(attachment);
                        }
                        passtkt.setAttachments(passattach);

                    }else{
                        passtkt.setAttachments(null);
                    }
                    List<Attachment> attachments1 = checkall.getAttachments();
                    if(attachments1!=null) {
                        List<Attachment> passattach = new ArrayList<>();
                        for (Attachment attachment : attachments1) {
                            attachment.setTicketDt(null);
                            attachment.setFilePath(request.getRequestURL().toString().replace("/getAllTkt", "/downloadAttachment/" + attachment.getId()));
                            passattach.add(attachment);
                        }
                        passtkt.setAttachments(passattach);
                    }


                    passtkt.setCreatedon(checkall.getCreatedon());
                    passtkt.setReportedon(checkall.getReportedon());
                    pass.add(passtkt);

                }

                return pass;
            }else{
                return null;
            }



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    ///employee

    public  List<mTicketSdeatils> getAllTicketEmp(String cmpName,HttpServletRequest request){
        try{

            List<mTicketSdeatils> all = ticketDRepos.findByCompanyname(cmpName);

            if(all!=null){
                List<mTicketSdeatils> pass= new ArrayList<>();
                for(mTicketSdeatils checkall: all)
                {
                    mTicketSdeatils passtkt = new mTicketSdeatils();
                    passtkt.setTicketid(checkall.getTicketid());
                    passtkt.setTicketcode(checkall.getTicketcode());

                    mGeneralMaster tickettype = checkall.getTickettype();
                    if(tickettype!=null){
                        mGeneralMaster passType = new mGeneralMaster();
                        passType.setGmid(tickettype.getGmid());
                        passType.setGmType(tickettype.getGmType());
                        passType.setGmDescription(tickettype.getGmDescription());
                        passtkt.setTickettype(passType);

                    }

                    mGeneralMaster ticketlevel = checkall.getTicketlevel();
                    if(ticketlevel!=null){
                        mGeneralMaster passLevel = new mGeneralMaster();
                        passLevel.setGmid(ticketlevel.getGmid());
                        passLevel.setGmType(ticketlevel.getGmType());
                        passLevel.setGmDescription(ticketlevel.getGmDescription());
                        passtkt.setTicketlevel(passLevel);

                    }
                    mGeneralMaster priority = checkall.getPriority();
                    if(priority!=null){
                        mGeneralMaster passPriority = new mGeneralMaster();
                        passPriority.setGmid(priority.getGmid());
                        passPriority.setGmType(priority.getGmType());
                        passPriority.setGmDescription(priority.getGmDescription());
                        passtkt.setPriority(passPriority);

                    }
                    mGeneralMaster status = checkall.getStatus();
                    if(status!=null){
                        mGeneralMaster passStatus = new mGeneralMaster();
                        passStatus.setGmid(status.getGmid());
                        passStatus.setGmType(status.getGmType());
                        passStatus.setGmDescription(status.getGmDescription());
                        passtkt.setStatus(passStatus);
                    }
                    passtkt.setCompanyname(checkall.getCompanyname());
                    mClientMaster clientid = checkall.getClientid();
                    if(clientid!=null){
                        mClientMaster passClient = new mClientMaster();
                        passClient.setCompanyMaster(null);
                        passClient.setMClientCMEMasterList(null);
                        List<mClientSPOCMaster> mClientSPOCMasters = clientid.getMClientSPOCMasters();
                        if(mClientSPOCMasters!=null){
                            List<mClientSPOCMaster> newpassspocclient =new ArrayList<>();
                            for(mClientSPOCMaster checkclientspoc:mClientSPOCMasters) {
                                checkclientspoc.setClientmasterId(null);
                                newpassspocclient.add(checkclientspoc);

                            }
                            passClient.setMClientSPOCMasters(newpassspocclient);

                        }else{
                            passClient.setMClientSPOCMasters(null);
                        }
                        passClient.setClientId(clientid.getClientId());
                        passClient.setClientName(clientid.getClientName());
                        passClient.setClientCode(clientid.getClientCode());
                        passClient.setClientGroup(clientid.getClientGroup());
                        passClient.setClientModule(clientid.getClientModule());
                        passClient.setIsactive(clientid.getIsactive());
                        passClient.setInsertdtm(clientid.getInsertdtm());
                        passClient.setUpdatedtm(clientid.getUpdatedtm());




                        passtkt.setClientid(passClient);
                    }
                    passtkt.setTicketnote(checkall.getTicketnote());
                    passtkt.setTicketdesc(checkall.getTicketdesc());
                    mModulesMaster modulesid = checkall.getModulesid();
                    if(modulesid!=null){
                        mModulesMaster passmod= new mModulesMaster();
                        passmod.setModId(modulesid.getModId());
                        passmod.setModcode(modulesid.getModcode());
                        passmod.setIsactive(modulesid.getIsactive());
                        passmod.setCompanyMaster(null);
                        passmod.setEmployeeMasterList(null);
                        passmod.setMClientCMEMasterList(null);
                        passtkt.setModulesid(passmod);
                    }else{
                        passtkt.setModulesid(null);
                    }

                    mClientCMEMaster cmexpertId = checkall.getCmexpertId();
                    if(cmexpertId!=null){
                        mClientCMEMaster passcme= new mClientCMEMaster();
                        passcme.setCmeId(cmexpertId.getCmeId());
                        passcme.setCmeName(cmexpertId.getCmeName());
                        passcme.setCmeDesignation(cmexpertId.getCmeDesignation());
                        passcme.setCmeemailId(cmexpertId.getCmeemailId());
                        passcme.setCmephoneNo(cmexpertId.getCmephoneNo());
                        passcme.setIsActive(cmexpertId.getIsActive());
                        passcme.setInsertdtm(cmexpertId.getInsertdtm());
                        passcme.setUpdatedtm(cmexpertId.getUpdatedtm());
                        passcme.setClientMasterIdCme(null);
                        passcme.setCmemodulesMaster(null);
                        passtkt.setCmexpertId(passcme);
                    }else{
                        passtkt.setCmexpertId(null);
                    }

                    mEmployeeMaster employeeId = checkall.getEmployeeId();
                    if(employeeId!=null){
                        mEmployeeMaster passemp = new mEmployeeMaster();
                        passemp.setModulesMaster_id(null);
                        passemp.setRoleMaster_id(null);
                        passemp.setInsertDtm(null);
                        passemp.setUpdateDtm(null);
                        passemp.setIsActive(null);
                        passemp.setDateOfJoining(null);
                        passemp.setEmpId(employeeId.getEmpId());
                        passemp.setEmpCode(employeeId.getEmpCode());
                        passemp.setIsActive(employeeId.getIsActive());
                        passemp.setEmpName(employeeId.getEmpName());
                        passemp.setEmailId(employeeId.getEmailId());
                        passemp.setPhoneNo(employeeId.getPhoneNo());
                        passemp.setCompanyName(employeeId.getCompanyName());
                        passtkt.setEmployeeId(passemp);
                    }else{
                        passtkt.setEmployeeId(null);
                    }

                    List<Attachment> attachments = checkall.getAttachments();
                    if(attachments!=null){
                        List<Attachment> passattach = new ArrayList<>();
                        for(Attachment attachment: attachments){
                            attachment.setTicketDt(null);
                            passattach.add(attachment);
                        }
                        passtkt.setAttachments(passattach);

                    }else{
                        passtkt.setAttachments(null);
                    }
                    List<Attachment> attachments1 = checkall.getAttachments();
                    if(attachments1!=null) {
                        List<Attachment> passattach = new ArrayList<>();
                        for (Attachment attachment : attachments1) {
                            attachment.setTicketDt(null);
                            attachment.setFilePath(request.getRequestURL().toString().replace("/getAllTkt", "/downloadAttachment/" + attachment.getId()));
                            passattach.add(attachment);
                        }
                        passtkt.setAttachments(passattach);
                    }


                    passtkt.setCreatedon(checkall.getCreatedon());
                    passtkt.setReportedon(checkall.getReportedon());
                    pass.add(passtkt);

                }

                return pass;
            }else{
                return null;
            }



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public  List<mTicketSdeatils> getNotAssignTicketEmp(String cmpName,HttpServletRequest request){
        try{

            List<mTicketSdeatils> all = ticketDRepos.findNOTAssignByCompanyname(cmpName);

            if(all!=null){
                List<mTicketSdeatils> pass= new ArrayList<>();
                for(mTicketSdeatils checkall: all)
                {
                    mTicketSdeatils passtkt = new mTicketSdeatils();
                    passtkt.setTicketid(checkall.getTicketid());
                    passtkt.setTicketcode(checkall.getTicketcode());

                    mGeneralMaster tickettype = checkall.getTickettype();
                    if(tickettype!=null){
                        mGeneralMaster passType = new mGeneralMaster();
                        passType.setGmid(tickettype.getGmid());
                        passType.setGmType(tickettype.getGmType());
                        passType.setGmDescription(tickettype.getGmDescription());
                        passtkt.setTickettype(passType);

                    }

                    mGeneralMaster ticketlevel = checkall.getTicketlevel();
                    if(ticketlevel!=null){
                        mGeneralMaster passLevel = new mGeneralMaster();
                        passLevel.setGmid(ticketlevel.getGmid());
                        passLevel.setGmType(ticketlevel.getGmType());
                        passLevel.setGmDescription(ticketlevel.getGmDescription());
                        passtkt.setTicketlevel(passLevel);

                    }
                    mGeneralMaster priority = checkall.getPriority();
                    if(priority!=null){
                        mGeneralMaster passPriority = new mGeneralMaster();
                        passPriority.setGmid(priority.getGmid());
                        passPriority.setGmType(priority.getGmType());
                        passPriority.setGmDescription(priority.getGmDescription());
                        passtkt.setPriority(passPriority);

                    }
                    mGeneralMaster status = checkall.getStatus();
                    if(status!=null){
                        mGeneralMaster passStatus = new mGeneralMaster();
                        passStatus.setGmid(status.getGmid());
                        passStatus.setGmType(status.getGmType());
                        passStatus.setGmDescription(status.getGmDescription());
                        passtkt.setStatus(passStatus);
                    }
                    passtkt.setCompanyname(checkall.getCompanyname());
                    mClientMaster clientid = checkall.getClientid();
                    if(clientid!=null){
                        mClientMaster passClient = new mClientMaster();
                        passClient.setCompanyMaster(null);
                        passClient.setMClientCMEMasterList(null);
                        List<mClientSPOCMaster> mClientSPOCMasters = clientid.getMClientSPOCMasters();
                        if(mClientSPOCMasters!=null){
                            List<mClientSPOCMaster> newpassspocclient =new ArrayList<>();
                            for(mClientSPOCMaster checkclientspoc:mClientSPOCMasters) {
                                checkclientspoc.setClientmasterId(null);
                                newpassspocclient.add(checkclientspoc);

                            }
                            passClient.setMClientSPOCMasters(newpassspocclient);

                        }else{
                            passClient.setMClientSPOCMasters(null);
                        }
                        passClient.setClientId(clientid.getClientId());
                        passClient.setClientName(clientid.getClientName());
                        passClient.setClientCode(clientid.getClientCode());
                        passClient.setClientGroup(clientid.getClientGroup());
                        passClient.setClientModule(clientid.getClientModule());
                        passClient.setIsactive(clientid.getIsactive());
                        passClient.setInsertdtm(clientid.getInsertdtm());
                        passClient.setUpdatedtm(clientid.getUpdatedtm());




                        passtkt.setClientid(passClient);
                    }
                    passtkt.setTicketnote(checkall.getTicketnote());
                    passtkt.setTicketdesc(checkall.getTicketdesc());
                    mModulesMaster modulesid = checkall.getModulesid();
                    if(modulesid!=null){
                        mModulesMaster passmod= new mModulesMaster();
                        passmod.setModId(modulesid.getModId());
                        passmod.setModcode(modulesid.getModcode());
                        passmod.setIsactive(modulesid.getIsactive());
                        passmod.setCompanyMaster(null);
                        passmod.setEmployeeMasterList(null);
                        passmod.setMClientCMEMasterList(null);
                        passtkt.setModulesid(passmod);
                    }else{
                        passtkt.setModulesid(null);
                    }

                    mClientCMEMaster cmexpertId = checkall.getCmexpertId();
                    if(cmexpertId!=null){
                        mClientCMEMaster passcme= new mClientCMEMaster();
                        passcme.setCmeId(cmexpertId.getCmeId());
                        passcme.setCmeName(cmexpertId.getCmeName());
                        passcme.setCmeDesignation(cmexpertId.getCmeDesignation());
                        passcme.setCmeemailId(cmexpertId.getCmeemailId());
                        passcme.setCmephoneNo(cmexpertId.getCmephoneNo());
                        passcme.setIsActive(cmexpertId.getIsActive());
                        passcme.setInsertdtm(cmexpertId.getInsertdtm());
                        passcme.setUpdatedtm(cmexpertId.getUpdatedtm());
                        passcme.setClientMasterIdCme(null);
                        passcme.setCmemodulesMaster(null);
                        passtkt.setCmexpertId(passcme);
                    }else{
                        passtkt.setCmexpertId(null);
                    }

                    mEmployeeMaster employeeId = checkall.getEmployeeId();
                    if(employeeId!=null){
                        mEmployeeMaster passemp = new mEmployeeMaster();
                        passemp.setModulesMaster_id(null);
                        passemp.setRoleMaster_id(null);
                        passemp.setInsertDtm(null);
                        passemp.setUpdateDtm(null);
                        passemp.setIsActive(null);
                        passemp.setDateOfJoining(null);
                        passemp.setEmpId(employeeId.getEmpId());
                        passemp.setEmpCode(employeeId.getEmpCode());
                        passemp.setIsActive(employeeId.getIsActive());
                        passemp.setEmpName(employeeId.getEmpName());
                        passemp.setEmailId(employeeId.getEmailId());
                        passemp.setPhoneNo(employeeId.getPhoneNo());
                        passemp.setCompanyName(employeeId.getCompanyName());
                        passtkt.setEmployeeId(passemp);
                    }else{
                        passtkt.setEmployeeId(null);
                    }

                    List<Attachment> attachments = checkall.getAttachments();
                    if(attachments!=null){
                        List<Attachment> passattach = new ArrayList<>();
                        for(Attachment attachment: attachments){
                            attachment.setTicketDt(null);
                            passattach.add(attachment);
                        }
                        passtkt.setAttachments(passattach);

                    }else{
                        passtkt.setAttachments(null);
                    }
                    List<Attachment> attachments1 = checkall.getAttachments();
                    if(attachments1!=null) {
                        List<Attachment> passattach = new ArrayList<>();
                        for (Attachment attachment : attachments1) {
                            attachment.setTicketDt(null);
                            attachment.setFilePath(request.getRequestURL().toString().replace("/getAllTkt", "/downloadAttachment/" + attachment.getId()));
                            passattach.add(attachment);
                        }
                        passtkt.setAttachments(passattach);
                    }


                    passtkt.setCreatedon(checkall.getCreatedon());
                    passtkt.setReportedon(checkall.getReportedon());
                    pass.add(passtkt);

                }

                return pass;
            }else{
                return null;
            }



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public  List<mTicketSdeatils> getAssignTicketEmp(String cmpName,HttpServletRequest request){
        try{

            List<mTicketSdeatils> all = ticketDRepos.findAssignByCompanyname(cmpName);

            if(all!=null){
                List<mTicketSdeatils> pass= new ArrayList<>();
                for(mTicketSdeatils checkall: all)
                {
                    mTicketSdeatils passtkt = new mTicketSdeatils();
                    passtkt.setTicketid(checkall.getTicketid());
                    passtkt.setTicketcode(checkall.getTicketcode());

                    mGeneralMaster tickettype = checkall.getTickettype();
                    if(tickettype!=null){
                        mGeneralMaster passType = new mGeneralMaster();
                        passType.setGmid(tickettype.getGmid());
                        passType.setGmType(tickettype.getGmType());
                        passType.setGmDescription(tickettype.getGmDescription());
                        passtkt.setTickettype(passType);

                    }

                    mGeneralMaster ticketlevel = checkall.getTicketlevel();
                    if(ticketlevel!=null){
                        mGeneralMaster passLevel = new mGeneralMaster();
                        passLevel.setGmid(ticketlevel.getGmid());
                        passLevel.setGmType(ticketlevel.getGmType());
                        passLevel.setGmDescription(ticketlevel.getGmDescription());
                        passtkt.setTicketlevel(passLevel);

                    }
                    mGeneralMaster priority = checkall.getPriority();
                    if(priority!=null){
                        mGeneralMaster passPriority = new mGeneralMaster();
                        passPriority.setGmid(priority.getGmid());
                        passPriority.setGmType(priority.getGmType());
                        passPriority.setGmDescription(priority.getGmDescription());
                        passtkt.setPriority(passPriority);

                    }
                    mGeneralMaster status = checkall.getStatus();
                    if(status!=null){
                        mGeneralMaster passStatus = new mGeneralMaster();
                        passStatus.setGmid(status.getGmid());
                        passStatus.setGmType(status.getGmType());
                        passStatus.setGmDescription(status.getGmDescription());
                        passtkt.setStatus(passStatus);
                    }
                    passtkt.setCompanyname(checkall.getCompanyname());
                    mClientMaster clientid = checkall.getClientid();
                    if(clientid!=null){
                        mClientMaster passClient = new mClientMaster();
                        passClient.setCompanyMaster(null);
                        passClient.setMClientCMEMasterList(null);
                        List<mClientSPOCMaster> mClientSPOCMasters = clientid.getMClientSPOCMasters();
                        if(mClientSPOCMasters!=null){
                            List<mClientSPOCMaster> newpassspocclient =new ArrayList<>();
                            for(mClientSPOCMaster checkclientspoc:mClientSPOCMasters) {
                                checkclientspoc.setClientmasterId(null);
                                newpassspocclient.add(checkclientspoc);

                            }
                            passClient.setMClientSPOCMasters(newpassspocclient);

                        }else{
                            passClient.setMClientSPOCMasters(null);
                        }
                        passClient.setClientId(clientid.getClientId());
                        passClient.setClientName(clientid.getClientName());
                        passClient.setClientCode(clientid.getClientCode());
                        passClient.setClientGroup(clientid.getClientGroup());
                        passClient.setClientModule(clientid.getClientModule());
                        passClient.setIsactive(clientid.getIsactive());
                        passClient.setInsertdtm(clientid.getInsertdtm());
                        passClient.setUpdatedtm(clientid.getUpdatedtm());




                        passtkt.setClientid(passClient);
                    }
                    passtkt.setTicketnote(checkall.getTicketnote());
                    passtkt.setTicketdesc(checkall.getTicketdesc());
                    mModulesMaster modulesid = checkall.getModulesid();
                    if(modulesid!=null){
                        mModulesMaster passmod= new mModulesMaster();
                        passmod.setModId(modulesid.getModId());
                        passmod.setModcode(modulesid.getModcode());
                        passmod.setIsactive(modulesid.getIsactive());
                        passmod.setCompanyMaster(null);
                        passmod.setEmployeeMasterList(null);
                        passmod.setMClientCMEMasterList(null);
                        passtkt.setModulesid(passmod);
                    }else{
                        passtkt.setModulesid(null);
                    }

                    mClientCMEMaster cmexpertId = checkall.getCmexpertId();
                    if(cmexpertId!=null){
                        mClientCMEMaster passcme= new mClientCMEMaster();
                        passcme.setCmeId(cmexpertId.getCmeId());
                        passcme.setCmeName(cmexpertId.getCmeName());
                        passcme.setCmeDesignation(cmexpertId.getCmeDesignation());
                        passcme.setCmeemailId(cmexpertId.getCmeemailId());
                        passcme.setCmephoneNo(cmexpertId.getCmephoneNo());
                        passcme.setIsActive(cmexpertId.getIsActive());
                        passcme.setInsertdtm(cmexpertId.getInsertdtm());
                        passcme.setUpdatedtm(cmexpertId.getUpdatedtm());
                        passcme.setClientMasterIdCme(null);
                        passcme.setCmemodulesMaster(null);
                        passtkt.setCmexpertId(passcme);
                    }else{
                        passtkt.setCmexpertId(null);
                    }

                    mEmployeeMaster employeeId = checkall.getEmployeeId();
                    if(employeeId!=null){
                        mEmployeeMaster passemp = new mEmployeeMaster();
                        passemp.setModulesMaster_id(null);
                        passemp.setRoleMaster_id(null);
                        passemp.setInsertDtm(null);
                        passemp.setUpdateDtm(null);
                        passemp.setIsActive(null);
                        passemp.setDateOfJoining(null);
                        passemp.setEmpId(employeeId.getEmpId());
                        passemp.setEmpCode(employeeId.getEmpCode());
                        passemp.setIsActive(employeeId.getIsActive());
                        passemp.setEmpName(employeeId.getEmpName());
                        passemp.setEmailId(employeeId.getEmailId());
                        passemp.setPhoneNo(employeeId.getPhoneNo());
                        passemp.setCompanyName(employeeId.getCompanyName());
                        passtkt.setEmployeeId(passemp);
                    }else{
                        passtkt.setEmployeeId(null);
                    }

                    List<Attachment> attachments = checkall.getAttachments();
                    if(attachments!=null){
                        List<Attachment> passattach = new ArrayList<>();
                        for(Attachment attachment: attachments){
                            attachment.setTicketDt(null);
                            passattach.add(attachment);
                        }
                        passtkt.setAttachments(passattach);

                    }else{
                        passtkt.setAttachments(null);
                    }
                    List<Attachment> attachments1 = checkall.getAttachments();
                    if(attachments1!=null) {
                        List<Attachment> passattach = new ArrayList<>();
                        for (Attachment attachment : attachments1) {
                            attachment.setTicketDt(null);
                            attachment.setFilePath(request.getRequestURL().toString().replace("/getAllTkt", "/downloadAttachment/" + attachment.getId()));
                            passattach.add(attachment);
                        }
                        passtkt.setAttachments(passattach);
                    }


                    passtkt.setCreatedon(checkall.getCreatedon());
                    passtkt.setReportedon(checkall.getReportedon());
                    pass.add(passtkt);

                }

                return pass;
            }else{
                return null;
            }



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public  List<mTicketSdeatils> getAllTicketEmpByMod(Long modid,HttpServletRequest request){
        try{

            List<mTicketSdeatils> all = ticketDRepos.findBymodulesid(modid);

            if(all!=null){
                List<mTicketSdeatils> pass= new ArrayList<>();
                for(mTicketSdeatils checkall: all)
                {
                    mTicketSdeatils passtkt = new mTicketSdeatils();
                    passtkt.setTicketid(checkall.getTicketid());
                    passtkt.setTicketcode(checkall.getTicketcode());

                    mGeneralMaster tickettype = checkall.getTickettype();
                    if(tickettype!=null){
                        mGeneralMaster passType = new mGeneralMaster();
                        passType.setGmid(tickettype.getGmid());
                        passType.setGmType(tickettype.getGmType());
                        passType.setGmDescription(tickettype.getGmDescription());
                        passtkt.setTickettype(passType);

                    }

                    mGeneralMaster ticketlevel = checkall.getTicketlevel();
                    if(ticketlevel!=null){
                        mGeneralMaster passLevel = new mGeneralMaster();
                        passLevel.setGmid(ticketlevel.getGmid());
                        passLevel.setGmType(ticketlevel.getGmType());
                        passLevel.setGmDescription(ticketlevel.getGmDescription());
                        passtkt.setTicketlevel(passLevel);

                    }
                    mGeneralMaster priority = checkall.getPriority();
                    if(priority!=null){
                        mGeneralMaster passPriority = new mGeneralMaster();
                        passPriority.setGmid(priority.getGmid());
                        passPriority.setGmType(priority.getGmType());
                        passPriority.setGmDescription(priority.getGmDescription());
                        passtkt.setPriority(passPriority);

                    }
                    mGeneralMaster status = checkall.getStatus();
                    if(status!=null){
                        mGeneralMaster passStatus = new mGeneralMaster();
                        passStatus.setGmid(status.getGmid());
                        passStatus.setGmType(status.getGmType());
                        passStatus.setGmDescription(status.getGmDescription());
                        passtkt.setStatus(passStatus);
                    }
                    passtkt.setCompanyname(checkall.getCompanyname());
                    mClientMaster clientid = checkall.getClientid();
                    if(clientid!=null){
                        mClientMaster passClient = new mClientMaster();
                        passClient.setCompanyMaster(null);
                        passClient.setMClientCMEMasterList(null);
                        List<mClientSPOCMaster> mClientSPOCMasters = clientid.getMClientSPOCMasters();
                        if(mClientSPOCMasters!=null){
                            List<mClientSPOCMaster> newpassspocclient =new ArrayList<>();
                            for(mClientSPOCMaster checkclientspoc:mClientSPOCMasters) {
                                checkclientspoc.setClientmasterId(null);
                                newpassspocclient.add(checkclientspoc);

                            }
                            passClient.setMClientSPOCMasters(newpassspocclient);

                        }else{
                            passClient.setMClientSPOCMasters(null);
                        }
                        passClient.setClientId(clientid.getClientId());
                        passClient.setClientName(clientid.getClientName());
                        passClient.setClientCode(clientid.getClientCode());
                        passClient.setClientGroup(clientid.getClientGroup());
                        passClient.setClientModule(clientid.getClientModule());
                        passClient.setIsactive(clientid.getIsactive());
                        passClient.setInsertdtm(clientid.getInsertdtm());
                        passClient.setUpdatedtm(clientid.getUpdatedtm());




                        passtkt.setClientid(passClient);
                    }
                    passtkt.setTicketnote(checkall.getTicketnote());
                    passtkt.setTicketdesc(checkall.getTicketdesc());
                    mModulesMaster modulesid = checkall.getModulesid();
                    if(modulesid!=null){
                        mModulesMaster passmod= new mModulesMaster();
                        passmod.setModId(modulesid.getModId());
                        passmod.setModcode(modulesid.getModcode());
                        passmod.setIsactive(modulesid.getIsactive());
                        passmod.setCompanyMaster(null);
                        passmod.setEmployeeMasterList(null);
                        passmod.setMClientCMEMasterList(null);
                        passtkt.setModulesid(passmod);
                    }else{
                        passtkt.setModulesid(null);
                    }

                    mClientCMEMaster cmexpertId = checkall.getCmexpertId();
                    if(cmexpertId!=null){
                        mClientCMEMaster passcme= new mClientCMEMaster();
                        passcme.setCmeId(cmexpertId.getCmeId());
                        passcme.setCmeName(cmexpertId.getCmeName());
                        passcme.setCmeDesignation(cmexpertId.getCmeDesignation());
                        passcme.setCmeemailId(cmexpertId.getCmeemailId());
                        passcme.setCmephoneNo(cmexpertId.getCmephoneNo());
                        passcme.setIsActive(cmexpertId.getIsActive());
                        passcme.setInsertdtm(cmexpertId.getInsertdtm());
                        passcme.setUpdatedtm(cmexpertId.getUpdatedtm());
                        passcme.setClientMasterIdCme(null);
                        passcme.setCmemodulesMaster(null);
                        passtkt.setCmexpertId(passcme);
                    }else{
                        passtkt.setCmexpertId(null);
                    }

                    mEmployeeMaster employeeId = checkall.getEmployeeId();
                    if(employeeId!=null){
                        mEmployeeMaster passemp = new mEmployeeMaster();
                        passemp.setModulesMaster_id(null);
                        passemp.setRoleMaster_id(null);
                        passemp.setInsertDtm(null);
                        passemp.setUpdateDtm(null);
                        passemp.setIsActive(null);
                        passemp.setDateOfJoining(null);
                        passemp.setEmpId(employeeId.getEmpId());
                        passemp.setEmpCode(employeeId.getEmpCode());
                        passemp.setIsActive(employeeId.getIsActive());
                        passemp.setEmpName(employeeId.getEmpName());
                        passemp.setEmailId(employeeId.getEmailId());
                        passemp.setPhoneNo(employeeId.getPhoneNo());
                        passemp.setCompanyName(employeeId.getCompanyName());
                        passtkt.setEmployeeId(passemp);
                    }else{
                        passtkt.setEmployeeId(null);
                    }

                    List<Attachment> attachments = checkall.getAttachments();
                    if(attachments!=null){
                        List<Attachment> passattach = new ArrayList<>();
                        for(Attachment attachment: attachments){
                            attachment.setTicketDt(null);
                            passattach.add(attachment);
                        }
                        passtkt.setAttachments(passattach);

                    }else{
                        passtkt.setAttachments(null);
                    }
                    List<Attachment> attachments1 = checkall.getAttachments();
                    if(attachments1!=null) {
                        List<Attachment> passattach = new ArrayList<>();
                        for (Attachment attachment : attachments1) {
                            attachment.setTicketDt(null);
                            attachment.setFilePath(request.getRequestURL().toString().replace("/getAllTkt", "/downloadAttachment/" + attachment.getId()));
                            passattach.add(attachment);
                        }
                        passtkt.setAttachments(passattach);
                    }


                    passtkt.setCreatedon(checkall.getCreatedon());
                    passtkt.setReportedon(checkall.getReportedon());
                    pass.add(passtkt);

                }

                return pass;
            }else{
                return null;
            }



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public  List<mTicketSdeatils> getAssignTicketEmpByMod(Long modid,HttpServletRequest request){
        try{

            List<mTicketSdeatils> all = ticketDRepos.findAssignBymodulesid(modid);

            if(all!=null){
                List<mTicketSdeatils> pass= new ArrayList<>();
                for(mTicketSdeatils checkall: all)
                {
                    mTicketSdeatils passtkt = new mTicketSdeatils();
                    passtkt.setTicketid(checkall.getTicketid());
                    passtkt.setTicketcode(checkall.getTicketcode());

                    mGeneralMaster tickettype = checkall.getTickettype();
                    if(tickettype!=null){
                        mGeneralMaster passType = new mGeneralMaster();
                        passType.setGmid(tickettype.getGmid());
                        passType.setGmType(tickettype.getGmType());
                        passType.setGmDescription(tickettype.getGmDescription());
                        passtkt.setTickettype(passType);

                    }

                    mGeneralMaster ticketlevel = checkall.getTicketlevel();
                    if(ticketlevel!=null){
                        mGeneralMaster passLevel = new mGeneralMaster();
                        passLevel.setGmid(ticketlevel.getGmid());
                        passLevel.setGmType(ticketlevel.getGmType());
                        passLevel.setGmDescription(ticketlevel.getGmDescription());
                        passtkt.setTicketlevel(passLevel);

                    }
                    mGeneralMaster priority = checkall.getPriority();
                    if(priority!=null){
                        mGeneralMaster passPriority = new mGeneralMaster();
                        passPriority.setGmid(priority.getGmid());
                        passPriority.setGmType(priority.getGmType());
                        passPriority.setGmDescription(priority.getGmDescription());
                        passtkt.setPriority(passPriority);

                    }
                    mGeneralMaster status = checkall.getStatus();
                    if(status!=null){
                        mGeneralMaster passStatus = new mGeneralMaster();
                        passStatus.setGmid(status.getGmid());
                        passStatus.setGmType(status.getGmType());
                        passStatus.setGmDescription(status.getGmDescription());
                        passtkt.setStatus(passStatus);
                    }
                    passtkt.setCompanyname(checkall.getCompanyname());
                    mClientMaster clientid = checkall.getClientid();
                    if(clientid!=null){
                        mClientMaster passClient = new mClientMaster();
                        passClient.setCompanyMaster(null);
                        passClient.setMClientCMEMasterList(null);
                        List<mClientSPOCMaster> mClientSPOCMasters = clientid.getMClientSPOCMasters();
                        if(mClientSPOCMasters!=null){
                            List<mClientSPOCMaster> newpassspocclient =new ArrayList<>();
                            for(mClientSPOCMaster checkclientspoc:mClientSPOCMasters) {
                                checkclientspoc.setClientmasterId(null);
                                newpassspocclient.add(checkclientspoc);

                            }
                            passClient.setMClientSPOCMasters(newpassspocclient);

                        }else{
                            passClient.setMClientSPOCMasters(null);
                        }
                        passClient.setClientId(clientid.getClientId());
                        passClient.setClientName(clientid.getClientName());
                        passClient.setClientCode(clientid.getClientCode());
                        passClient.setClientGroup(clientid.getClientGroup());
                        passClient.setClientModule(clientid.getClientModule());
                        passClient.setIsactive(clientid.getIsactive());
                        passClient.setInsertdtm(clientid.getInsertdtm());
                        passClient.setUpdatedtm(clientid.getUpdatedtm());




                        passtkt.setClientid(passClient);
                    }
                    passtkt.setTicketnote(checkall.getTicketnote());
                    passtkt.setTicketdesc(checkall.getTicketdesc());
                    mModulesMaster modulesid = checkall.getModulesid();
                    if(modulesid!=null){
                        mModulesMaster passmod= new mModulesMaster();
                        passmod.setModId(modulesid.getModId());
                        passmod.setModcode(modulesid.getModcode());
                        passmod.setIsactive(modulesid.getIsactive());
                        passmod.setCompanyMaster(null);
                        passmod.setEmployeeMasterList(null);
                        passmod.setMClientCMEMasterList(null);
                        passtkt.setModulesid(passmod);
                    }else{
                        passtkt.setModulesid(null);
                    }

                    mClientCMEMaster cmexpertId = checkall.getCmexpertId();
                    if(cmexpertId!=null){
                        mClientCMEMaster passcme= new mClientCMEMaster();
                        passcme.setCmeId(cmexpertId.getCmeId());
                        passcme.setCmeName(cmexpertId.getCmeName());
                        passcme.setCmeDesignation(cmexpertId.getCmeDesignation());
                        passcme.setCmeemailId(cmexpertId.getCmeemailId());
                        passcme.setCmephoneNo(cmexpertId.getCmephoneNo());
                        passcme.setIsActive(cmexpertId.getIsActive());
                        passcme.setInsertdtm(cmexpertId.getInsertdtm());
                        passcme.setUpdatedtm(cmexpertId.getUpdatedtm());
                        passcme.setClientMasterIdCme(null);
                        passcme.setCmemodulesMaster(null);
                        passtkt.setCmexpertId(passcme);
                    }else{
                        passtkt.setCmexpertId(null);
                    }

                    mEmployeeMaster employeeId = checkall.getEmployeeId();
                    if(employeeId!=null){
                        mEmployeeMaster passemp = new mEmployeeMaster();
                        passemp.setModulesMaster_id(null);
                        passemp.setRoleMaster_id(null);
                        passemp.setInsertDtm(null);
                        passemp.setUpdateDtm(null);
                        passemp.setIsActive(null);
                        passemp.setDateOfJoining(null);
                        passemp.setEmpId(employeeId.getEmpId());
                        passemp.setEmpCode(employeeId.getEmpCode());
                        passemp.setIsActive(employeeId.getIsActive());
                        passemp.setEmpName(employeeId.getEmpName());
                        passemp.setEmailId(employeeId.getEmailId());
                        passemp.setPhoneNo(employeeId.getPhoneNo());
                        passemp.setCompanyName(employeeId.getCompanyName());
                        passtkt.setEmployeeId(passemp);
                    }else{
                        passtkt.setEmployeeId(null);
                    }

                    List<Attachment> attachments = checkall.getAttachments();
                    if(attachments!=null){
                        List<Attachment> passattach = new ArrayList<>();
                        for(Attachment attachment: attachments){
                            attachment.setTicketDt(null);
                            passattach.add(attachment);
                        }
                        passtkt.setAttachments(passattach);

                    }else{
                        passtkt.setAttachments(null);
                    }
                    List<Attachment> attachments1 = checkall.getAttachments();
                    if(attachments1!=null) {
                        List<Attachment> passattach = new ArrayList<>();
                        for (Attachment attachment : attachments1) {
                            attachment.setTicketDt(null);
                            attachment.setFilePath(request.getRequestURL().toString().replace("/getAllTkt", "/downloadAttachment/" + attachment.getId()));
                            passattach.add(attachment);
                        }
                        passtkt.setAttachments(passattach);
                    }


                    passtkt.setCreatedon(checkall.getCreatedon());
                    passtkt.setReportedon(checkall.getReportedon());
                    pass.add(passtkt);

                }

                return pass;
            }else{
                return null;
            }



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public  List<mTicketSdeatils> getNOTAssignTicketEmpByMod(Long modid,HttpServletRequest request){
        try{

            List<mTicketSdeatils> all = ticketDRepos.findNOTAssignBymodulesid(modid);

            if(all!=null){
                List<mTicketSdeatils> pass= new ArrayList<>();
                for(mTicketSdeatils checkall: all)
                {
                    mTicketSdeatils passtkt = new mTicketSdeatils();
                    passtkt.setTicketid(checkall.getTicketid());
                    passtkt.setTicketcode(checkall.getTicketcode());

                    mGeneralMaster tickettype = checkall.getTickettype();
                    if(tickettype!=null){
                        mGeneralMaster passType = new mGeneralMaster();
                        passType.setGmid(tickettype.getGmid());
                        passType.setGmType(tickettype.getGmType());
                        passType.setGmDescription(tickettype.getGmDescription());
                        passtkt.setTickettype(passType);

                    }

                    mGeneralMaster ticketlevel = checkall.getTicketlevel();
                    if(ticketlevel!=null){
                        mGeneralMaster passLevel = new mGeneralMaster();
                        passLevel.setGmid(ticketlevel.getGmid());
                        passLevel.setGmType(ticketlevel.getGmType());
                        passLevel.setGmDescription(ticketlevel.getGmDescription());
                        passtkt.setTicketlevel(passLevel);

                    }
                    mGeneralMaster priority = checkall.getPriority();
                    if(priority!=null){
                        mGeneralMaster passPriority = new mGeneralMaster();
                        passPriority.setGmid(priority.getGmid());
                        passPriority.setGmType(priority.getGmType());
                        passPriority.setGmDescription(priority.getGmDescription());
                        passtkt.setPriority(passPriority);

                    }
                    mGeneralMaster status = checkall.getStatus();
                    if(status!=null){
                        mGeneralMaster passStatus = new mGeneralMaster();
                        passStatus.setGmid(status.getGmid());
                        passStatus.setGmType(status.getGmType());
                        passStatus.setGmDescription(status.getGmDescription());
                        passtkt.setStatus(passStatus);
                    }
                    passtkt.setCompanyname(checkall.getCompanyname());
                    mClientMaster clientid = checkall.getClientid();
                    if(clientid!=null){
                        mClientMaster passClient = new mClientMaster();
                        passClient.setCompanyMaster(null);
                        passClient.setMClientCMEMasterList(null);
                        List<mClientSPOCMaster> mClientSPOCMasters = clientid.getMClientSPOCMasters();
                        if(mClientSPOCMasters!=null){
                            List<mClientSPOCMaster> newpassspocclient =new ArrayList<>();
                            for(mClientSPOCMaster checkclientspoc:mClientSPOCMasters) {
                                checkclientspoc.setClientmasterId(null);
                                newpassspocclient.add(checkclientspoc);

                            }
                            passClient.setMClientSPOCMasters(newpassspocclient);

                        }else{
                            passClient.setMClientSPOCMasters(null);
                        }
                        passClient.setClientId(clientid.getClientId());
                        passClient.setClientName(clientid.getClientName());
                        passClient.setClientCode(clientid.getClientCode());
                        passClient.setClientGroup(clientid.getClientGroup());
                        passClient.setClientModule(clientid.getClientModule());
                        passClient.setIsactive(clientid.getIsactive());
                        passClient.setInsertdtm(clientid.getInsertdtm());
                        passClient.setUpdatedtm(clientid.getUpdatedtm());




                        passtkt.setClientid(passClient);
                    }
                    passtkt.setTicketnote(checkall.getTicketnote());
                    passtkt.setTicketdesc(checkall.getTicketdesc());
                    mModulesMaster modulesid = checkall.getModulesid();
                    if(modulesid!=null){
                        mModulesMaster passmod= new mModulesMaster();
                        passmod.setModId(modulesid.getModId());
                        passmod.setModcode(modulesid.getModcode());
                        passmod.setIsactive(modulesid.getIsactive());
                        passmod.setCompanyMaster(null);
                        passmod.setEmployeeMasterList(null);
                        passmod.setMClientCMEMasterList(null);
                        passtkt.setModulesid(passmod);
                    }else{
                        passtkt.setModulesid(null);
                    }

                    mClientCMEMaster cmexpertId = checkall.getCmexpertId();
                    if(cmexpertId!=null){
                        mClientCMEMaster passcme= new mClientCMEMaster();
                        passcme.setCmeId(cmexpertId.getCmeId());
                        passcme.setCmeName(cmexpertId.getCmeName());
                        passcme.setCmeDesignation(cmexpertId.getCmeDesignation());
                        passcme.setCmeemailId(cmexpertId.getCmeemailId());
                        passcme.setCmephoneNo(cmexpertId.getCmephoneNo());
                        passcme.setIsActive(cmexpertId.getIsActive());
                        passcme.setInsertdtm(cmexpertId.getInsertdtm());
                        passcme.setUpdatedtm(cmexpertId.getUpdatedtm());
                        passcme.setClientMasterIdCme(null);
                        passcme.setCmemodulesMaster(null);
                        passtkt.setCmexpertId(passcme);
                    }else{
                        passtkt.setCmexpertId(null);
                    }

                    mEmployeeMaster employeeId = checkall.getEmployeeId();
                    if(employeeId!=null){
                        mEmployeeMaster passemp = new mEmployeeMaster();
                        passemp.setModulesMaster_id(null);
                        passemp.setRoleMaster_id(null);
                        passemp.setInsertDtm(null);
                        passemp.setUpdateDtm(null);
                        passemp.setIsActive(null);
                        passemp.setDateOfJoining(null);
                        passemp.setEmpId(employeeId.getEmpId());
                        passemp.setEmpCode(employeeId.getEmpCode());
                        passemp.setIsActive(employeeId.getIsActive());
                        passemp.setEmpName(employeeId.getEmpName());
                        passemp.setEmailId(employeeId.getEmailId());
                        passemp.setPhoneNo(employeeId.getPhoneNo());
                        passemp.setCompanyName(employeeId.getCompanyName());
                        passtkt.setEmployeeId(passemp);
                    }else{
                        passtkt.setEmployeeId(null);
                    }

                    List<Attachment> attachments = checkall.getAttachments();
                    if(attachments!=null){
                        List<Attachment> passattach = new ArrayList<>();
                        for(Attachment attachment: attachments){
                            attachment.setTicketDt(null);
                            passattach.add(attachment);
                        }
                        passtkt.setAttachments(passattach);

                    }else{
                        passtkt.setAttachments(null);
                    }
                    List<Attachment> attachments1 = checkall.getAttachments();
                    if(attachments1!=null) {
                        List<Attachment> passattach = new ArrayList<>();
                        for (Attachment attachment : attachments1) {
                            attachment.setTicketDt(null);
                            attachment.setFilePath(request.getRequestURL().toString().replace("/getAllTkt", "/downloadAttachment/" + attachment.getId()));
                            passattach.add(attachment);
                        }
                        passtkt.setAttachments(passattach);
                    }


                    passtkt.setCreatedon(checkall.getCreatedon());
                    passtkt.setReportedon(checkall.getReportedon());
                    pass.add(passtkt);

                }

                return pass;
            }else{
                return null;
            }



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public mTicketSdeatils assignEmpByEmp(Long tcktID,Integer empID,HttpServletRequest request ){
        try{

            mEmployeeMaster employeeById = employeeService.getEmployeeById(empID);
            if(employeeById!=null) {
//                mTicketSdeatils getticketbyid = getticketbyid(tcktID, request);
                mTicketSdeatils getticketbyid = ticketDRepos.findById(tcktID).orElse(null);
                if (getticketbyid != null) {
                    getticketbyid.setEmployeeId(employeeById);
                    mTicketSdeatils checkall = ticketDRepos.save(getticketbyid);

                    if (checkall != null) {


                        mTicketSdeatils passtkt = new mTicketSdeatils();
                        passtkt.setTicketid(checkall.getTicketid());
                        passtkt.setTicketcode(checkall.getTicketcode());

                        mGeneralMaster tickettype = checkall.getTickettype();
                        if (tickettype != null) {
                            mGeneralMaster passType = new mGeneralMaster();
                            passType.setGmid(tickettype.getGmid());
                            passType.setGmType(tickettype.getGmType());
                            passType.setGmDescription(tickettype.getGmDescription());
                            passtkt.setTickettype(passType);

                        }

                        mGeneralMaster ticketlevel = checkall.getTicketlevel();
                        if (ticketlevel != null) {
                            mGeneralMaster passLevel = new mGeneralMaster();
                            passLevel.setGmid(ticketlevel.getGmid());
                            passLevel.setGmType(ticketlevel.getGmType());
                            passLevel.setGmDescription(ticketlevel.getGmDescription());
                            passtkt.setTicketlevel(passLevel);

                        }
                        mGeneralMaster priority = checkall.getPriority();
                        if (priority != null) {
                            mGeneralMaster passPriority = new mGeneralMaster();
                            passPriority.setGmid(priority.getGmid());
                            passPriority.setGmType(priority.getGmType());
                            passPriority.setGmDescription(priority.getGmDescription());
                            passtkt.setPriority(passPriority);

                        }
                        mGeneralMaster status = checkall.getStatus();
                        if (status != null) {
                            mGeneralMaster passStatus = new mGeneralMaster();
                            passStatus.setGmid(status.getGmid());
                            passStatus.setGmType(status.getGmType());
                            passStatus.setGmDescription(status.getGmDescription());
                            passtkt.setStatus(passStatus);
                        }
                        passtkt.setCompanyname(checkall.getCompanyname());
                        mClientMaster clientid = checkall.getClientid();
                        if (clientid != null) {
                            mClientMaster passClient = new mClientMaster();
                            passClient.setCompanyMaster(null);
                            passClient.setMClientCMEMasterList(null);
                            List<mClientSPOCMaster> mClientSPOCMasters = clientid.getMClientSPOCMasters();
                            if (mClientSPOCMasters != null) {
                                List<mClientSPOCMaster> newpassspocclient = new ArrayList<>();
                                for (mClientSPOCMaster checkclientspoc : mClientSPOCMasters) {
                                    checkclientspoc.setClientmasterId(null);
                                    newpassspocclient.add(checkclientspoc);

                                }
                                passClient.setMClientSPOCMasters(newpassspocclient);

                            } else {
                                passClient.setMClientSPOCMasters(null);
                            }
                            passClient.setClientId(clientid.getClientId());
                            passClient.setClientName(clientid.getClientName());
                            passClient.setClientCode(clientid.getClientCode());
                            passClient.setClientGroup(clientid.getClientGroup());
                            passClient.setClientModule(clientid.getClientModule());
                            passClient.setIsactive(clientid.getIsactive());
                            passClient.setInsertdtm(clientid.getInsertdtm());
                            passClient.setUpdatedtm(clientid.getUpdatedtm());


                            passtkt.setClientid(passClient);
                        }
                        passtkt.setTicketnote(checkall.getTicketnote());
                        passtkt.setTicketdesc(checkall.getTicketdesc());
                        mModulesMaster modulesid = checkall.getModulesid();
                        if (modulesid != null) {
                            mModulesMaster passmod = new mModulesMaster();
                            passmod.setModId(modulesid.getModId());
                            passmod.setModcode(modulesid.getModcode());
                            passmod.setIsactive(modulesid.getIsactive());
                            passmod.setCompanyMaster(null);
                            passmod.setEmployeeMasterList(null);
                            passmod.setMClientCMEMasterList(null);
                            passtkt.setModulesid(passmod);
                        } else {
                            passtkt.setModulesid(null);
                        }

                        mClientCMEMaster cmexpertId = checkall.getCmexpertId();
                        if (cmexpertId != null) {
                            mClientCMEMaster passcme = new mClientCMEMaster();
                            passcme.setCmeId(cmexpertId.getCmeId());
                            passcme.setCmeName(cmexpertId.getCmeName());
                            passcme.setCmeDesignation(cmexpertId.getCmeDesignation());
                            passcme.setCmeemailId(cmexpertId.getCmeemailId());
                            passcme.setCmephoneNo(cmexpertId.getCmephoneNo());
                            passcme.setIsActive(cmexpertId.getIsActive());
                            passcme.setInsertdtm(cmexpertId.getInsertdtm());
                            passcme.setUpdatedtm(cmexpertId.getUpdatedtm());
                            passcme.setClientMasterIdCme(null);
                            passcme.setCmemodulesMaster(null);
                            passtkt.setCmexpertId(passcme);
                        } else {
                            passtkt.setCmexpertId(null);
                        }

                        mEmployeeMaster employeeId = checkall.getEmployeeId();
                        if (employeeId != null) {
                            mEmployeeMaster passemp = new mEmployeeMaster();
                            passemp.setModulesMaster_id(null);
                            passemp.setRoleMaster_id(null);
                            passemp.setInsertDtm(null);
                            passemp.setUpdateDtm(null);
                            passemp.setIsActive(null);
                            passemp.setDateOfJoining(null);
                            passemp.setEmpId(employeeId.getEmpId());
                            passemp.setEmpCode(employeeId.getEmpCode());
                            passemp.setIsActive(employeeId.getIsActive());
                            passemp.setEmpName(employeeId.getEmpName());
                            passemp.setEmailId(employeeId.getEmailId());
                            passemp.setPhoneNo(employeeId.getPhoneNo());
                            passemp.setCompanyName(employeeId.getCompanyName());
                            passtkt.setEmployeeId(passemp);
                        } else {
                            passtkt.setEmployeeId(null);
                        }

                        List<Attachment> attachments1 = passtkt.getAttachments();
                        if (attachments1 != null) {
                            List<Attachment> passattach = new ArrayList<>();
                            for (Attachment attachment : attachments1) {
                                attachment.setTicketDt(null);
                                attachment.setFilePath(request.getRequestURL().toString().replace("/getTicketWithAttachments/" + tcktID, "/downloadAttachment/" + attachment.getId()));
                                passattach.add(attachment);
                            }
                            passtkt.setAttachments(passattach);

                        } else {
                            passtkt.setAttachments(null);
                        }
                        passtkt.setCreatedon(checkall.getCreatedon());
                        passtkt.setReportedon(checkall.getReportedon());

                        return passtkt;


                    } else {
                        throw new RuntimeException("not updated ticket");
                    }


                } else {
                    throw new RuntimeException("not found ticket");
                }

            }else {
                throw new RuntimeException("not found employee");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public mTicketSdeatils updateStatusByemp(Long tcktID,Integer empID,HttpServletRequest request ){
        try{

            mGeneralMaster employeeById = masterServices.getbyidGeneralMaster(empID);

            if(employeeById!=null) {
//                mTicketSdeatils getticketbyid = getticketbyid(tcktID, request);


                mTicketSdeatils getticketbyid = ticketDRepos.findById(tcktID).orElse(null);
                if (getticketbyid != null) {
                    getticketbyid.setStatus(employeeById);


                    mTicketSdeatils checkall = ticketDRepos.save(getticketbyid);

                    if (checkall != null) {


                        mTicketSdeatils passtkt = new mTicketSdeatils();
                        passtkt.setTicketid(checkall.getTicketid());
                        passtkt.setTicketcode(checkall.getTicketcode());

                        mGeneralMaster tickettype = checkall.getTickettype();
                        if (tickettype != null) {
                            mGeneralMaster passType = new mGeneralMaster();
                            passType.setGmid(tickettype.getGmid());
                            passType.setGmType(tickettype.getGmType());
                            passType.setGmDescription(tickettype.getGmDescription());
                            passtkt.setTickettype(passType);

                        }

                        mGeneralMaster ticketlevel = checkall.getTicketlevel();
                        if (ticketlevel != null) {
                            mGeneralMaster passLevel = new mGeneralMaster();
                            passLevel.setGmid(ticketlevel.getGmid());
                            passLevel.setGmType(ticketlevel.getGmType());
                            passLevel.setGmDescription(ticketlevel.getGmDescription());
                            passtkt.setTicketlevel(passLevel);

                        }
                        mGeneralMaster priority = checkall.getPriority();
                        if (priority != null) {
                            mGeneralMaster passPriority = new mGeneralMaster();
                            passPriority.setGmid(priority.getGmid());
                            passPriority.setGmType(priority.getGmType());
                            passPriority.setGmDescription(priority.getGmDescription());
                            passtkt.setPriority(passPriority);

                        }
                        mGeneralMaster status = checkall.getStatus();
                        if (status != null) {
                            mGeneralMaster passStatus = new mGeneralMaster();
                            passStatus.setGmid(status.getGmid());
                            passStatus.setGmType(status.getGmType());
                            passStatus.setGmDescription(status.getGmDescription());
                            passtkt.setStatus(passStatus);
                        }
                        passtkt.setCompanyname(checkall.getCompanyname());
                        mClientMaster clientid = checkall.getClientid();
                        if (clientid != null) {
                            mClientMaster passClient = new mClientMaster();
                            passClient.setCompanyMaster(null);
                            passClient.setMClientCMEMasterList(null);
                            List<mClientSPOCMaster> mClientSPOCMasters = clientid.getMClientSPOCMasters();
                            if (mClientSPOCMasters != null) {
                                List<mClientSPOCMaster> newpassspocclient = new ArrayList<>();
                                for (mClientSPOCMaster checkclientspoc : mClientSPOCMasters) {
                                    checkclientspoc.setClientmasterId(null);
                                    newpassspocclient.add(checkclientspoc);

                                }
                                passClient.setMClientSPOCMasters(newpassspocclient);

                            } else {
                                passClient.setMClientSPOCMasters(null);
                            }
                            passClient.setClientId(clientid.getClientId());
                            passClient.setClientName(clientid.getClientName());
                            passClient.setClientCode(clientid.getClientCode());
                            passClient.setClientGroup(clientid.getClientGroup());
                            passClient.setClientModule(clientid.getClientModule());
                            passClient.setIsactive(clientid.getIsactive());
                            passClient.setInsertdtm(clientid.getInsertdtm());
                            passClient.setUpdatedtm(clientid.getUpdatedtm());


                            passtkt.setClientid(passClient);
                        }
                        passtkt.setTicketnote(checkall.getTicketnote());
                        passtkt.setTicketdesc(checkall.getTicketdesc());
                        mModulesMaster modulesid = checkall.getModulesid();
                        if (modulesid != null) {
                            mModulesMaster passmod = new mModulesMaster();
                            passmod.setModId(modulesid.getModId());
                            passmod.setModcode(modulesid.getModcode());
                            passmod.setIsactive(modulesid.getIsactive());
                            passmod.setCompanyMaster(null);
                            passmod.setEmployeeMasterList(null);
                            passmod.setMClientCMEMasterList(null);
                            passtkt.setModulesid(passmod);
                        } else {
                            passtkt.setModulesid(null);
                        }

                        mClientCMEMaster cmexpertId = checkall.getCmexpertId();
                        if (cmexpertId != null) {
                            mClientCMEMaster passcme = new mClientCMEMaster();
                            passcme.setCmeId(cmexpertId.getCmeId());
                            passcme.setCmeName(cmexpertId.getCmeName());
                            passcme.setCmeDesignation(cmexpertId.getCmeDesignation());
                            passcme.setCmeemailId(cmexpertId.getCmeemailId());
                            passcme.setCmephoneNo(cmexpertId.getCmephoneNo());
                            passcme.setIsActive(cmexpertId.getIsActive());
                            passcme.setInsertdtm(cmexpertId.getInsertdtm());
                            passcme.setUpdatedtm(cmexpertId.getUpdatedtm());
                            passcme.setClientMasterIdCme(null);
                            passcme.setCmemodulesMaster(null);
                            passtkt.setCmexpertId(passcme);
                        } else {
                            passtkt.setCmexpertId(null);
                        }

                        mEmployeeMaster employeeId = checkall.getEmployeeId();
                        if (employeeId != null) {
                            mEmployeeMaster passemp = new mEmployeeMaster();
                            passemp.setModulesMaster_id(null);
                            passemp.setRoleMaster_id(null);
                            passemp.setInsertDtm(null);
                            passemp.setUpdateDtm(null);
                            passemp.setIsActive(null);
                            passemp.setDateOfJoining(null);
                            passemp.setEmpId(employeeId.getEmpId());
                            passemp.setEmpCode(employeeId.getEmpCode());
                            passemp.setIsActive(employeeId.getIsActive());
                            passemp.setEmpName(employeeId.getEmpName());
                            passemp.setEmailId(employeeId.getEmailId());
                            passemp.setPhoneNo(employeeId.getPhoneNo());
                            passemp.setCompanyName(employeeId.getCompanyName());
                            passtkt.setEmployeeId(passemp);
                        } else {
                            passtkt.setEmployeeId(null);
                        }

                        List<Attachment> attachments1 = passtkt.getAttachments();
                        if (attachments1 != null) {
                            List<Attachment> passattach = new ArrayList<>();
                            for (Attachment attachment : attachments1) {
                                attachment.setTicketDt(null);
                                attachment.setFilePath(request.getRequestURL().toString().replace("/getTicketWithAttachments/" + tcktID, "/downloadAttachment/" + attachment.getId()));
                                passattach.add(attachment);
                            }
                            passtkt.setAttachments(passattach);

                        } else {
                            passtkt.setAttachments(null);
                        }
                        passtkt.setCreatedon(checkall.getCreatedon());
                        passtkt.setReportedon(checkall.getReportedon());

                        return passtkt;


                    } else {
                        throw new RuntimeException("not updated ticket");
                    }


                } else {
                    throw new RuntimeException("not found ticket");
                }

            }else {
                throw new RuntimeException("not found employee");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//Client
@Autowired
private AttachmentMasteRepo attachmentMasteRepo;


//    public void addAttachmentByClientOnhold()




}