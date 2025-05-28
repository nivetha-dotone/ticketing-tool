package com.dot1.ticket_track.services;

import com.dot1.ticket_track.entity.*;
import com.dot1.ticket_track.repository.CmeMstRepo;
import com.dot1.ticket_track.repository.UserLogin_demoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class clientcmeService {

    @Autowired
    private CmeMstRepo cmeMstRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserLogin_demoRepo userLoginDemoRepo;

    public mClientCMEMaster addcmeclient(mClientCMEMaster master){
        try{

            master.setCmeId(cmeMstRepo.newcmeID());
            mClientCMEMaster save = adduserlogin(master);
            if(save!=null){
                master.setCmeId(cmeMstRepo.newcmeID());

                return save;

            }else{
                return null;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }

    public mClientCMEMaster adduserlogin(mClientCMEMaster employee) {
        try {
            mClientCMEMaster saved = cmeMstRepo.save(employee);
            if(saved!=null){

            mUserLogin_demo mUserLoginDemo = new mUserLogin_demo();
            mUserLoginDemo.setLogId(userLoginDemoRepo.newIdUserLogin());
            mUserLoginDemo.setUserID(saved.getCmeemailId());
            mUserLoginDemo.setUserPWD(passwordEncoder.encode("Client@12345"));
            mUserLoginDemo.setCmeMaster(saved);
            mUserLoginDemo.setIsactUser(false);
            mUserLoginDemo.setRole("CLIENT");
            mUserLogin_demo save = userLoginDemoRepo.save(mUserLoginDemo);
            if(save!=null){
                if(saved!=null){


                    return saved;

                }else{
                    return null;
                }
            }else{
                return  null;
            }


            }else{
                return null;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public mClientCMEMaster updateCMEclient(Integer cmeId,mClientCMEMaster updatedCME){
        try{
//            oldLogin.setPassword(!newlogin.getPassword().equals(" ")&&newlogin.getPassword()!=null?newlogin.getPassword():oldLogin.getPassword());

            mClientCMEMaster oldCME = getCmeByIdforupdate(cmeId);
            if(oldCME!=null){
                oldCME.setCmeName( !updatedCME.getCmeName().equals(" ")&&oldCME.getCmeName()!=null?updatedCME.getCmeName():oldCME.getCmeName());
                oldCME.setCmeDesignation( !updatedCME.getCmeDesignation().equals(" ")&&oldCME.getCmeDesignation()!=null?updatedCME.getCmeDesignation():oldCME.getCmeDesignation());
                oldCME.setCmeemailId( !updatedCME.getCmeemailId().equals(" ")&&oldCME.getCmeemailId()!=null?updatedCME.getCmeemailId():oldCME.getCmeemailId());
                oldCME.setCmephoneNo( !updatedCME.getCmephoneNo().equals(" ")&&oldCME.getCmephoneNo()!=null?updatedCME.getCmephoneNo():oldCME.getCmephoneNo());
                if(updatedCME.getIsActive()!=null){
                    oldCME.setIsActive(updatedCME.getIsActive());
                }
                oldCME.setClientMasterIdCme(updatedCME.getClientMasterIdCme());
                oldCME.setCmemodulesMaster(updatedCME.getCmemodulesMaster());
            }

            mClientCMEMaster save = cmeMstRepo.save(oldCME);
            return save;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<mClientCMEMaster> getAllClients() {
        try {
            List<mClientCMEMaster> getAll = cmeMstRepo.findAll();
            if (getAll != null) {
            List<mClientCMEMaster> passedAll=new ArrayList<>();
            for(mClientCMEMaster checked: getAll )
            {
                if(checked!=null){
                mClientCMEMaster fitnewCMEMaster = new mClientCMEMaster();
                fitnewCMEMaster.setCmeId(checked.getCmeId());
                fitnewCMEMaster.setCmeName(checked.getCmeName());
                fitnewCMEMaster.setCmeDesignation(checked.getCmeDesignation());
                fitnewCMEMaster.setCmeemailId(checked.getCmeemailId());
                fitnewCMEMaster.setCmephoneNo(checked.getCmephoneNo());

                    mClientMaster clientMasterIdCme = checked.getClientMasterIdCme();
                    if(clientMasterIdCme!=null){
                        mClientMaster fitnewclient =  new mClientMaster();
                        fitnewclient.setClientId(clientMasterIdCme.getClientId());
                        fitnewclient.setClientCode(clientMasterIdCme.getClientCode());
                        fitnewclient.setClientName(clientMasterIdCme.getClientName());
                        fitnewclient.setClientGroup(clientMasterIdCme.getClientGroup());
                        fitnewclient.setClientModule(clientMasterIdCme.getClientModule());
                        fitnewclient.setIsactive(clientMasterIdCme.getIsactive());
                        fitnewclient.setEmployeeClient(clientMasterIdCme.getEmployeeClient());
                        mCompanyMaster companyMaster = clientMasterIdCme.getCompanyMaster();
                        if(companyMaster!=null){
                            mCompanyMaster fitnewcomp = new mCompanyMaster();
                            fitnewcomp.setMModulesMasters(null);
                            fitnewcomp.setClientMasters(null);
                            fitnewcomp.setCmpid(companyMaster.getCmpid());
                            fitnewcomp.setCmpcode(companyMaster.getCmpcode());
                            fitnewcomp.setCmpnm(companyMaster.getCmpnm());
                            fitnewcomp.setIsactive(companyMaster.getIsactive());
                            fitnewcomp.setInsdate(companyMaster.getInsdate());
                            fitnewclient.setCompanyMaster(fitnewcomp);
                        }
                        List<mClientSPOCMaster> mClientSPOCMasters = clientMasterIdCme.getMClientSPOCMasters();
                        if(mClientSPOCMasters!=null){
                            List<mClientSPOCMaster> fitnewspoc =new ArrayList<>();
                            for(mClientSPOCMaster checkspoc: mClientSPOCMasters){
                                mClientSPOCMaster fitnewaddspoc = new mClientSPOCMaster();
                                fitnewaddspoc.setSpocId(checkspoc.getSpocId());
                                fitnewaddspoc.setSpocName(checkspoc.getSpocName());
                                fitnewaddspoc.setDesignation(checkspoc.getDesignation());
                                fitnewaddspoc.setEmailId(checkspoc.getEmailId());
                                fitnewaddspoc.setContactNumber(checkspoc.getContactNumber());
                                fitnewaddspoc.setIsActive(checkspoc.getIsActive());
                                fitnewaddspoc.setClientmasterId(null);
                                fitnewaddspoc.setInsertdtm(null);
                                fitnewaddspoc.setUpdatedtm(null);
                                fitnewspoc.add(fitnewaddspoc);

                            }

                            clientMasterIdCme.setMClientSPOCMasters(fitnewspoc);

                        }
                        mModulesMaster cmemodulesMaster = checked.getCmemodulesMaster();
                        if (cmemodulesMaster!=null){
                            mModulesMaster fitnewmodule =new mModulesMaster();
                            fitnewmodule.setModId(cmemodulesMaster.getModId());
                            fitnewmodule.setModcode(cmemodulesMaster.getModcode());
                            fitnewmodule.setIsactive(cmemodulesMaster.getIsactive());
                            fitnewmodule.setCompanyMaster(null);
                            fitnewmodule.setEmployeeMasterList(null);
                            fitnewmodule.setMClientCMEMasterList(null);
                            checked.setCmemodulesMaster(fitnewmodule);

                        }
                        fitnewclient.setMClientCMEMasterList(null);
                        checked.setClientMasterIdCme(fitnewclient);
                    }
                }
                passedAll.add(checked);
            }
            }
            if (getAll != null) {
                return getAll;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public mClientCMEMaster getCmeById(Integer id) {
        try {

            mClientCMEMaster checked = cmeMstRepo.findById(id).orElse(null);
            if(checked!=null){
                mClientCMEMaster fitnewCMEMaster = new mClientCMEMaster();
                fitnewCMEMaster.setCmeId(checked.getCmeId());
                fitnewCMEMaster.setCmeName(checked.getCmeName());
                fitnewCMEMaster.setCmeDesignation(checked.getCmeDesignation());
                fitnewCMEMaster.setCmeemailId(checked.getCmeemailId());
                fitnewCMEMaster.setCmephoneNo(checked.getCmephoneNo());

                mClientMaster clientMasterIdCme = checked.getClientMasterIdCme();
                if(clientMasterIdCme!=null){
                    mClientMaster fitnewclient =  new mClientMaster();
                    fitnewclient.setClientId(clientMasterIdCme.getClientId());
                    fitnewclient.setClientCode(clientMasterIdCme.getClientCode());
                    fitnewclient.setEmployeeClient(clientMasterIdCme.getEmployeeClient());
                    fitnewclient.setClientName(clientMasterIdCme.getClientName());
                    fitnewclient.setClientGroup(clientMasterIdCme.getClientGroup());
                    fitnewclient.setClientModule(clientMasterIdCme.getClientModule());
                    fitnewclient.setIsactive(clientMasterIdCme.getIsactive());
                    mCompanyMaster companyMaster = clientMasterIdCme.getCompanyMaster();
                    if(companyMaster!=null){
                        mCompanyMaster fitnewcomp = new mCompanyMaster();
                        fitnewcomp.setMModulesMasters(null);
                        fitnewcomp.setClientMasters(null);
                        fitnewcomp.setCmpid(companyMaster.getCmpid());
                        fitnewcomp.setCmpcode(companyMaster.getCmpcode());
                        fitnewcomp.setCmpnm(companyMaster.getCmpnm());
                        fitnewcomp.setIsactive(companyMaster.getIsactive());
                        fitnewcomp.setInsdate(companyMaster.getInsdate());
                        fitnewclient.setCompanyMaster(fitnewcomp);
                    }
                    List<mClientSPOCMaster> mClientSPOCMasters = clientMasterIdCme.getMClientSPOCMasters();
                    if(mClientSPOCMasters!=null){
                        List<mClientSPOCMaster> fitnewspoc =new ArrayList<>();
                        for(mClientSPOCMaster checkspoc: mClientSPOCMasters){
                            mClientSPOCMaster fitnewaddspoc = new mClientSPOCMaster();
                            fitnewaddspoc.setSpocId(checkspoc.getSpocId());
                            fitnewaddspoc.setSpocName(checkspoc.getSpocName());
                            fitnewaddspoc.setDesignation(checkspoc.getDesignation());
                            fitnewaddspoc.setEmailId(checkspoc.getEmailId());
                            fitnewaddspoc.setContactNumber(checkspoc.getContactNumber());
                            fitnewaddspoc.setIsActive(checkspoc.getIsActive());
                            fitnewaddspoc.setClientmasterId(null);
                            fitnewaddspoc.setInsertdtm(null);
                            fitnewaddspoc.setUpdatedtm(null);
                            fitnewspoc.add(fitnewaddspoc);
                        }
                        clientMasterIdCme.setMClientSPOCMasters(fitnewspoc);

                    }
                    mModulesMaster cmemodulesMaster = checked.getCmemodulesMaster();
                    if (cmemodulesMaster!=null){
                        mModulesMaster fitnewmodule =new mModulesMaster();
                        fitnewmodule.setModId(cmemodulesMaster.getModId());
                        fitnewmodule.setModcode(cmemodulesMaster.getModcode());
                        fitnewmodule.setIsactive(cmemodulesMaster.getIsactive());
                        fitnewmodule.setCompanyMaster(null);
                        fitnewmodule.setEmployeeMasterList(null);
                        fitnewmodule.setMClientCMEMasterList(null);
                        checked.setCmemodulesMaster(fitnewmodule);

                    }
                    fitnewclient.setMClientCMEMasterList(null);
                    checked.setClientMasterIdCme(fitnewclient);
                }
                return checked;

            }else{
                return null;
            }



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public mClientCMEMaster getCmeByIdforupdate(Integer id) {
        try {
            mClientCMEMaster cmeMaster = cmeMstRepo.findById(id).orElse(null);
if(cmeMaster!=null){
    return cmeMaster;

}else{
    return  null;
}

//            mClientCMEMaster checked = cmeMstRepo.findById(id).orElse(null);
//            if(checked!=null){
//                mClientCMEMaster fitnewCMEMaster = new mClientCMEMaster();
//                fitnewCMEMaster.setCmeId(checked.getCmeId());
//                fitnewCMEMaster.setCmeName(checked.getCmeName());
//                fitnewCMEMaster.setCmeDesignation(checked.getCmeDesignation());
//                fitnewCMEMaster.setCmeemailId(checked.getCmeemailId());
//                fitnewCMEMaster.setCmephoneNo(checked.getCmephoneNo());
//
//                mClientMaster clientMasterIdCme = checked.getClientMasterIdCme();
//                if(clientMasterIdCme!=null){
//                    mClientMaster fitnewclient =  new mClientMaster();
//                    fitnewclient.setClientId(clientMasterIdCme.getClientId());
//                    fitnewclient.setClientCode(clientMasterIdCme.getClientCode());
//                    fitnewclient.setClientName(clientMasterIdCme.getClientName());
//                    fitnewclient.setClientGroup(clientMasterIdCme.getClientGroup());
//                    fitnewclient.setClientModule(clientMasterIdCme.getClientModule());
//                    fitnewclient.setIsActive(clientMasterIdCme.getIsactive());
//                    mCompanyMaster companyMaster = clientMasterIdCme.getCompanyMaster();
//                    if(companyMaster!=null){
//                        mCompanyMaster fitnewcomp = new mCompanyMaster();
//                        fitnewcomp.setMModulesMasters(null);
//                        fitnewcomp.setClientMasters(null);
//                        fitnewcomp.setCmpid(companyMaster.getCmpid());
//                        fitnewcomp.setCmpcode(companyMaster.getCmpcode());
//                        fitnewcomp.setCmpnm(companyMaster.getCmpnm());
//                        fitnewcomp.setIsActive(companyMaster.getIsactive());
//                        fitnewcomp.setInsdate(companyMaster.getInsdate());
//                        fitnewclient.setCompanyMaster(fitnewcomp);
//                    }
//                    List<mClientSPOCMaster> mClientSPOCMasters = clientMasterIdCme.getMClientSPOCMasters();
//                    if(mClientSPOCMasters!=null){
//                        List<mClientSPOCMaster> fitnewspoc =new ArrayList<>();
//                        for(mClientSPOCMaster checkspoc: mClientSPOCMasters){
//                            mClientSPOCMaster fitnewaddspoc = new mClientSPOCMaster();
//                            fitnewaddspoc.setSpocId(checkspoc.getSpocId());
//                            fitnewaddspoc.setSpocName(checkspoc.getSpocName());
//                            fitnewaddspoc.setDesignation(checkspoc.getDesignation());
//                            fitnewaddspoc.setEmailId(checkspoc.getEmailId());
//                            fitnewaddspoc.setContactNumber(checkspoc.getContactNumber());
//                            fitnewaddspoc.setIsActive(checkspoc.getIsActive());
//                            fitnewaddspoc.setClientmasterId(null);
//                            fitnewaddspoc.setInsertdtm(null);
//                            fitnewaddspoc.setUpdatedtm(null);
//                            fitnewspoc.add(fitnewaddspoc);
//                        }
//                        clientMasterIdCme.setMClientSPOCMasters(fitnewspoc);
//
//                    }
//                    mModulesMaster cmemodulesMaster = checked.getCmemodulesMaster();
//                    if (cmemodulesMaster!=null){
//                        mModulesMaster fitnewmodule =new mModulesMaster();
//                        fitnewmodule.setModId(cmemodulesMaster.getModId());
//                        fitnewmodule.setModcode(cmemodulesMaster.getModcode());
//                        fitnewmodule.setIsactive(cmemodulesMaster.getIsactive());
//                        fitnewmodule.setCompanyMaster(null);
//                        fitnewmodule.setEmployeeMasterList(null);
//                        fitnewmodule.setMClientCMEMasterList(null);
//                        checked.setCmemodulesMaster(fitnewmodule);
//
//                    }
//                    fitnewclient.setMClientCMEMasterList(null);
//                    checked.setClientMasterIdCme(fitnewclient);
//                }
//                return checked;
//
//            }else{
//                return null;
//            }



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


@Autowired
private ClientService clientService;

    public mClientMaster getCmeByMod(Integer cmpid) {
        try {
            mClientMaster clientByIDforcme = clientService.getClientByIDforcme(cmpid);
            if(clientByIDforcme!=null){
                return clientByIDforcme;
            }
            else{
                return  null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<mClientMaster> getAllCMSPresentsclient() {
        try {
            List<mClientMaster> clientByIDforcme = clientService.getAllCMEPresntClients();
            if (clientByIDforcme != null) {
                return clientByIDforcme;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public mClientMaster getCMSPresentsclientByid(Integer id) {
            try {
                mClientMaster clientPresentCMEByID = clientService.getClientPresentCMEByID(id);
                if (clientPresentCMEByID != null) {
                    return clientPresentCMEByID;
                } else {
                    return null;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

    }

    public mClientMaster getActiveCMSPresentsclientByid(Integer id) {
            try {
                mClientMaster clientPresentCMEByID = clientService.getClientPresentActiveCMEByID(id);
                if (clientPresentCMEByID != null) {
                    return clientPresentCMEByID;
                } else {
                    return null;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

    }

    public mClientMaster getInactiveCMSPresentsclientByid(Integer id) {
            try {
                mClientMaster clientPresentCMEByID = clientService.getClientPresentInActiveCMEByID(id);
                if (clientPresentCMEByID != null) {
                    return clientPresentCMEByID;
                } else {
                    return null;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        @Autowired
        private ModuleServices moduleServices;
        public mModulesMaster getAllCmeInmodule(Integer modID){
            try{
                mModulesMaster bymodCME = moduleServices.getBymodCME(modID);
                if(bymodCME!=null){
                    return bymodCME;
                }else{
                    return null;
                }


            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }




}
