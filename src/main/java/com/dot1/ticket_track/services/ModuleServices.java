package com.dot1.ticket_track.services;


import com.dot1.ticket_track.entity.*;
import com.dot1.ticket_track.repository.CompanyRepository;
import com.dot1.ticket_track.repository.ModuleMstRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModuleServices {

    @Autowired
    private ModuleMstRepo moduleMstRepo;

    public mModulesMaster createMaster(mModulesMaster modules)
    {
        try{
            if(modules!=null) {
                Long i = moduleMstRepo.newIdMod();
                modules.setModId(i);
                mModulesMaster save = moduleMstRepo.save(modules);
                return save;
            }else{
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<mModulesMaster>  getAllMaster(){
        try{
            List<mModulesMaster> allModule = moduleMstRepo.findAll();
            List<mModulesMaster> passNew= new ArrayList<>();
                for(mModulesMaster checked: allModule ) {
                    mCompanyMaster companyMasterNew = checked.getCompanyMaster();
                    if (companyMasterNew != null) {
                        mCompanyMaster fitnew=new mCompanyMaster();
                        fitnew.setCmpid(companyMasterNew.getCmpid());
                        fitnew.setCmpcode(companyMasterNew.getCmpcode());
                        fitnew.setCmpnm(companyMasterNew.getCmpnm());
                        fitnew.setClientMasters(null);
                        fitnew.setMModulesMasters(null);
                        fitnew.setIsactive(companyMasterNew.getIsactive());

                        checked.setCompanyMaster(fitnew);

                    }else{
                        checked.setCompanyMaster(null);
                    }
                    List<mEmployeeMaster> employeeMasterList = checked.getEmployeeMasterList();

                    if(employeeMasterList!=null){
                        List<mEmployeeMaster> passedMaster=new ArrayList<>();
                        for(mEmployeeMaster checkedEmp: employeeMasterList){
                            mRoleMaster roleMasterId = checkedEmp.getRoleMaster_id();
                            roleMasterId.setEmployeeMasterList(null);
                            checkedEmp.setRoleMaster_id(roleMasterId);
                            passedMaster.add(checkedEmp);
                            checked.setEmployeeMasterList(passedMaster);
                        }
                    }else{
                        checked.setEmployeeMasterList(null);
                    }

                    List<mClientCMEMaster> mClientCMEMasterList = checked.getMClientCMEMasterList();
                    if(mClientCMEMasterList!=null){
                        List<mClientCMEMaster> fitnecclient= new ArrayList<>();
                        for( mClientCMEMaster cmeMaster: mClientCMEMasterList){
                                mClientCMEMaster pass = new mClientCMEMaster();
                                pass.setCmeId(cmeMaster.getCmeId());
                                pass.setCmeName(cmeMaster.getCmeName());
                                pass.setCmeDesignation(cmeMaster.getCmeDesignation());
                                pass.setCmeemailId(cmeMaster.getCmeemailId());
                                pass.setCmephoneNo(cmeMaster.getCmephoneNo());
                            mClientMaster clientMasterIdCme = cmeMaster.getClientMasterIdCme();
                            if (clientMasterIdCme!=null){
                                clientMasterIdCme.setCompanyMaster(null);
                                clientMasterIdCme.setMClientSPOCMasters(null);
                                clientMasterIdCme.setMClientCMEMasterList(null);
                            }
                            pass.setCmeId(cmeMaster.getCmeId());
                            fitnecclient.add(pass);

                        }
                        checked.setMClientCMEMasterList(fitnecclient);
                    }


                    passNew.add(checked);
                }
                return passNew;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public mModulesMaster  getByName(String mod_name)
    {
        try{
            mModulesMaster checked = moduleMstRepo.findBymodcode(mod_name).orElse(null);
            mCompanyMaster companyMasterNew = checked.getCompanyMaster();
            if (companyMasterNew != null) {
                mCompanyMaster fitnew=new mCompanyMaster();
                fitnew.setCmpid(companyMasterNew.getCmpid());
                fitnew.setCmpcode(companyMasterNew.getCmpcode());
                fitnew.setCmpnm(companyMasterNew.getCmpnm());
                fitnew.setClientMasters(null);
                fitnew.setMModulesMasters(null);
                fitnew.setIsactive(companyMasterNew.getIsactive());

                checked.setCompanyMaster(fitnew);

            }else{
                checked.setCompanyMaster(null);
            }
            List<mEmployeeMaster> employeeMasterList = checked.getEmployeeMasterList();

            if(employeeMasterList!=null){
                List<mEmployeeMaster> passedMaster=new ArrayList<>();
                for(mEmployeeMaster checkedEmp: employeeMasterList){
                    mRoleMaster roleMasterId = checkedEmp.getRoleMaster_id();
                    roleMasterId.setEmployeeMasterList(null);
                    checkedEmp.setRoleMaster_id(roleMasterId);
                    passedMaster.add(checkedEmp);
                    checked.setEmployeeMasterList(passedMaster);
                }
            }else{
                checked.setEmployeeMasterList(null);
            }
            List<mClientCMEMaster> mClientCMEMasterList = checked.getMClientCMEMasterList();
            if(mClientCMEMasterList!=null){
                List<mClientCMEMaster> fitnecclient= new ArrayList<>();
                for( mClientCMEMaster cmeMaster: mClientCMEMasterList){
                    mClientCMEMaster pass = new mClientCMEMaster();
                    pass.setCmeId(cmeMaster.getCmeId());
                    pass.setCmeName(cmeMaster.getCmeName());
                    pass.setCmeDesignation(cmeMaster.getCmeDesignation());
                    pass.setCmeemailId(cmeMaster.getCmeemailId());
                    pass.setCmephoneNo(cmeMaster.getCmephoneNo());
                    mClientMaster clientMasterIdCme = cmeMaster.getClientMasterIdCme();
                    if (clientMasterIdCme!=null){
                        clientMasterIdCme.setCompanyMaster(null);
                        clientMasterIdCme.setMClientSPOCMasters(null);
                        clientMasterIdCme.setMClientCMEMasterList(null);
                    }
                    pass.setCmeId(cmeMaster.getCmeId());
                    fitnecclient.add(pass);

                }
                checked.setMClientCMEMasterList(fitnecclient);
            }

            return checked;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


 public List<mClientCMEMaster>  getCmeByModName(String mod_name)
    {
        try{
            mModulesMaster checked = moduleMstRepo.findBymodcode(mod_name).orElse(null);

                checked.setCompanyMaster(null);

                checked.setEmployeeMasterList(null);
            List<mClientCMEMaster> mClientCMEMasterList = checked.getMClientCMEMasterList();
            if(mClientCMEMasterList!=null){
                List<mClientCMEMaster> fitnecclient= new ArrayList<>();
                for( mClientCMEMaster cmeMaster: mClientCMEMasterList){
                    mClientCMEMaster pass = new mClientCMEMaster();
                    pass.setCmeId(cmeMaster.getCmeId());
                    pass.setCmeName(cmeMaster.getCmeName());
                    pass.setCmeDesignation(cmeMaster.getCmeDesignation());
                    pass.setCmeemailId(cmeMaster.getCmeemailId());
                    pass.setCmephoneNo(cmeMaster.getCmephoneNo());
                    mClientMaster clientMasterIdCme = cmeMaster.getClientMasterIdCme();
                    if (clientMasterIdCme!=null){
                        clientMasterIdCme.setCompanyMaster(null);
                        clientMasterIdCme.setMClientSPOCMasters(null);
                        clientMasterIdCme.setMClientCMEMasterList(null);
                    }
                    pass.setCmeId(cmeMaster.getCmeId());
                    fitnecclient.add(pass);

                }
                checked.setMClientCMEMasterList(fitnecclient);
            }

            return checked.getMClientCMEMasterList();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<mEmployeeMaster>  getEmplistByModName(String mod_name)
    {
        try{
            mModulesMaster checked = moduleMstRepo.findBymodcode(mod_name).orElse(null);
          checked.setCompanyMaster(null);

            List<mEmployeeMaster> employeeMasterList = checked.getEmployeeMasterList();

            if(employeeMasterList!=null){
                List<mEmployeeMaster> passedMaster=new ArrayList<>();
                for(mEmployeeMaster checkedEmp: employeeMasterList){
                    checkedEmp.setRoleMaster_id(null);
                    checkedEmp.setModulesMaster_id(null);
                    passedMaster.add(checkedEmp);
                    checked.setEmployeeMasterList(passedMaster);
                }
            }else{
                checked.setEmployeeMasterList(null);
            }
                checked.setMClientCMEMasterList(null);


            return checked.getEmployeeMasterList();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
 public List<mEmployeeMaster>  getEmplistByModId(Long mod_name)
    {
        try{
            mModulesMaster checked = moduleMstRepo.findById(mod_name).orElse(null);
          checked.setCompanyMaster(null);

            List<mEmployeeMaster> employeeMasterList = checked.getEmployeeMasterList();

            if(employeeMasterList!=null){
                List<mEmployeeMaster> passedMaster=new ArrayList<>();
                for(mEmployeeMaster checkedEmp: employeeMasterList){
                    checkedEmp.setRoleMaster_id(null);
                    checkedEmp.setModulesMaster_id(null);
                    passedMaster.add(checkedEmp);
                    checked.setEmployeeMasterList(passedMaster);
                }
            }else{
                checked.setEmployeeMasterList(null);
            }
                checked.setMClientCMEMasterList(null);


            return checked.getEmployeeMasterList();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


  public List<mEmployeeMaster>  getEmplistByModNameForMGR(String mod_name)
    {
        try{
            mModulesMaster checked = moduleMstRepo.findBymodcode(mod_name).orElse(null);
          checked.setCompanyMaster(null);

            List<mEmployeeMaster> employeeMasterList = checked.getEmployeeMasterList();

            if(employeeMasterList!=null){
                List<mEmployeeMaster> passedMaster=new ArrayList<>();
                for(mEmployeeMaster checkedEmp: employeeMasterList){
                    checkedEmp.setRoleMaster_id(null);
                    checkedEmp.setModulesMaster_id(null);
                    passedMaster.add(checkedEmp);
                    checked.setEmployeeMasterList(passedMaster);
                }
            }else{
                checked.setEmployeeMasterList(null);
            }
                checked.setMClientCMEMasterList(null);


            return checked.getEmployeeMasterList();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public mModulesMaster  getByNameForPassIDINTOTKT(String mod_name)
    {
        try{
            mModulesMaster checked = moduleMstRepo.findBymodcode(mod_name).orElse(null);

                checked.setCompanyMaster(null);

                checked.setEmployeeMasterList(null);

            checked.setMClientCMEMasterList(null);

            return checked;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public mModulesMaster  getBymodId(long mod_name)
    {
        try{
            mModulesMaster checked = moduleMstRepo.findById(mod_name).orElse(null);
            mCompanyMaster companyMasterNew = checked.getCompanyMaster();
            if (companyMasterNew != null) {
                mCompanyMaster fitnew=new mCompanyMaster();
                fitnew.setCmpid(companyMasterNew.getCmpid());
                fitnew.setCmpcode(companyMasterNew.getCmpcode());
                fitnew.setCmpnm(companyMasterNew.getCmpnm());
                fitnew.setClientMasters(null);
                fitnew.setMModulesMasters(null);
                fitnew.setIsactive(companyMasterNew.getIsactive());

                checked.setCompanyMaster(fitnew);

            }else{
                checked.setCompanyMaster(null);
            }
            List<mEmployeeMaster> employeeMasterList = checked.getEmployeeMasterList();

            if(employeeMasterList!=null){
                List<mEmployeeMaster> passedMaster=new ArrayList<>();
                for(mEmployeeMaster checkedEmp: employeeMasterList){
                    mRoleMaster roleMasterId = checkedEmp.getRoleMaster_id();
                    roleMasterId.setEmployeeMasterList(null);
                    checkedEmp.setRoleMaster_id(roleMasterId);
                    passedMaster.add(checkedEmp);
                    checked.setEmployeeMasterList(passedMaster);
                }
            }else{
                checked.setEmployeeMasterList(null);
            }
            List<mClientCMEMaster> mClientCMEMasterList = checked.getMClientCMEMasterList();
            if(mClientCMEMasterList!=null){
                List<mClientCMEMaster> fitnecclient= new ArrayList<>();
                for( mClientCMEMaster cmeMaster: mClientCMEMasterList){
                    mClientCMEMaster pass = new mClientCMEMaster();
                    pass.setCmeId(cmeMaster.getCmeId());
                    pass.setCmeName(cmeMaster.getCmeName());
                    pass.setCmeDesignation(cmeMaster.getCmeDesignation());
                    pass.setCmeemailId(cmeMaster.getCmeemailId());
                    pass.setCmephoneNo(cmeMaster.getCmephoneNo());
                    mClientMaster clientMasterIdCme = cmeMaster.getClientMasterIdCme();
                    if (clientMasterIdCme!=null){
                        clientMasterIdCme.setCompanyMaster(null);
                        clientMasterIdCme.setMClientSPOCMasters(null);
                        clientMasterIdCme.setMClientCMEMasterList(null);
                    }
                    pass.setCmeId(cmeMaster.getCmeId());
                    fitnecclient.add(pass);

                }
                checked.setMClientCMEMasterList(fitnecclient);
            }

            return checked;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public mModulesMaster  getBymodCME(long mod_name) {
        try {
            mModulesMaster checked = moduleMstRepo.findById(mod_name).orElse(null);
            mCompanyMaster companyMasterNew = checked.getCompanyMaster();
            if (companyMasterNew != null) {
                mCompanyMaster fitnew = new mCompanyMaster();
                fitnew.setCmpid(companyMasterNew.getCmpid());
                fitnew.setCmpcode(companyMasterNew.getCmpcode());
                fitnew.setCmpnm(companyMasterNew.getCmpnm());
                fitnew.setClientMasters(null);
                fitnew.setMModulesMasters(null);
                fitnew.setIsactive(companyMasterNew.getIsactive());
                checked.setCompanyMaster(fitnew);
            } else {
                checked.setCompanyMaster(null);
            }
            checked.setEmployeeMasterList(null);

            List<mClientCMEMaster> getAll = checked.getMClientCMEMasterList();
            if (getAll != null) {
                List<mClientCMEMaster> passedAll = new ArrayList<>();
                for (mClientCMEMaster checkedcme : getAll) {
                    if (checkedcme != null) {
                        mClientCMEMaster fitnewCMEMaster = new mClientCMEMaster();
                        fitnewCMEMaster.setCmeId(checkedcme.getCmeId());
                        fitnewCMEMaster.setCmeName(checkedcme.getCmeName());
                        fitnewCMEMaster.setCmeDesignation(checkedcme.getCmeDesignation());
                        fitnewCMEMaster.setCmeemailId(checkedcme.getCmeemailId());
                        fitnewCMEMaster.setCmephoneNo(checkedcme.getCmephoneNo());

                        mClientMaster clientMasterIdCme = checkedcme.getClientMasterIdCme();
                        if (clientMasterIdCme != null) {
                            mClientMaster fitnewclient = new mClientMaster();
                            fitnewclient.setClientId(clientMasterIdCme.getClientId());
                            fitnewclient.setClientCode(clientMasterIdCme.getClientCode());
                            fitnewclient.setClientName(clientMasterIdCme.getClientName());
                            fitnewclient.setClientGroup(clientMasterIdCme.getClientGroup());
                            fitnewclient.setClientModule(clientMasterIdCme.getClientModule());
                            fitnewclient.setIsactive(clientMasterIdCme.getIsactive());
                            mCompanyMaster companyMaster = clientMasterIdCme.getCompanyMaster();
                            if (companyMaster != null) {
                                fitnewclient.setCompanyMaster(null);
                            }
                            List<mClientSPOCMaster> mClientSPOCMasters = clientMasterIdCme.getMClientSPOCMasters();
                            if (mClientSPOCMasters != null) {
                                List<mClientSPOCMaster> fitnewspoc = new ArrayList<>();
                                for (mClientSPOCMaster checkspoc : mClientSPOCMasters) {
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

                                fitnewclient.setMClientSPOCMasters(fitnewspoc);

                            }
                            fitnewCMEMaster.setCmemodulesMaster(null);
                            fitnewclient.setMClientCMEMasterList(null);
                            fitnewCMEMaster.setClientMasterIdCme(fitnewclient);

                        }
                        passedAll.add(fitnewCMEMaster);

                    }

                }


                checked.setMClientCMEMasterList(passedAll);

            }
            if(checked!=null){
                return checked;

            }else{
                return null;
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public mModulesMaster modulesMasterforEmployee(long id){
        try{
            mModulesMaster mModulesMaster = moduleMstRepo.findById(id).orElse(null);

            return mModulesMaster;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Autowired
    private CompanyService companyService;

        public List<mModulesMaster>  getByCmp_id(int mod_name) {
        try{
            mCompanyMaster companyById = companyService.getCompanyById(mod_name);
            List<mModulesMaster> allModule = companyById.getMModulesMasters();
            List<mModulesMaster> passNew= new ArrayList<>();
            for(mModulesMaster checked: allModule ) {
                mCompanyMaster companyMasterNew = checked.getCompanyMaster();
                if (companyMasterNew != null) {
                    mCompanyMaster fitnew=new mCompanyMaster();
                    fitnew.setCmpid(companyMasterNew.getCmpid());
                    fitnew.setCmpcode(companyMasterNew.getCmpcode());
                    fitnew.setCmpnm(companyMasterNew.getCmpnm());
                    fitnew.setClientMasters(null);
                    fitnew.setMModulesMasters(null);
                    fitnew.setIsactive(companyMasterNew.getIsactive());

                    checked.setCompanyMaster(fitnew);

                }else{
                    checked.setCompanyMaster(null);
                }
                List<mEmployeeMaster> employeeMasterList = checked.getEmployeeMasterList();

                if(employeeMasterList!=null){
                    List<mEmployeeMaster> passedMaster=new ArrayList<>();
                    for(mEmployeeMaster checkedEmp: employeeMasterList){
                        mRoleMaster roleMasterId = checkedEmp.getRoleMaster_id();
                        roleMasterId.setEmployeeMasterList(null);
                        checkedEmp.setRoleMaster_id(roleMasterId);
                        passedMaster.add(checkedEmp);
                        checked.setEmployeeMasterList(passedMaster);
                    }
                }else{
                    checked.setEmployeeMasterList(null);
                }
                List<mClientCMEMaster> mClientCMEMasterList = checked.getMClientCMEMasterList();
                if(mClientCMEMasterList!=null){
                    List<mClientCMEMaster> fitnecclient= new ArrayList<>();
                    for( mClientCMEMaster cmeMaster: mClientCMEMasterList){
                        mClientCMEMaster pass = new mClientCMEMaster();
                        pass.setCmeId(cmeMaster.getCmeId());
                        pass.setCmeName(cmeMaster.getCmeName());
                        pass.setCmeDesignation(cmeMaster.getCmeDesignation());
                        pass.setCmeemailId(cmeMaster.getCmeemailId());
                        pass.setCmephoneNo(cmeMaster.getCmephoneNo());
                        mClientMaster clientMasterIdCme = cmeMaster.getClientMasterIdCme();
                        if (clientMasterIdCme!=null){
                            clientMasterIdCme.setCompanyMaster(null);
                            clientMasterIdCme.setMClientSPOCMasters(null);
                            clientMasterIdCme.setMClientCMEMasterList(null);
                        }
                        pass.setCmeId(cmeMaster.getCmeId());
                        fitnecclient.add(pass);

                    }
                    checked.setMClientCMEMasterList(fitnecclient);
                }
                passNew.add(checked);
            }
            return passNew;



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    private CompanyRepository companyRepository;
        public List<mModulesMaster>  getByCmpName(String mod_name) {
        try{
            mCompanyMaster companyById = companyRepository.findByCompanyname(mod_name);
            List<mModulesMaster> allModule = companyById.getMModulesMasters();
            List<mModulesMaster> passNew= new ArrayList<>();
            for(mModulesMaster checked: allModule ) {
                checked.setCompanyMaster(null);



                    checked.setEmployeeMasterList(null);


                    checked.setMClientCMEMasterList(null);

                passNew.add(checked);
            }
            return passNew;



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

     public List<mModulesMaster>  getByCmp_idModulePresentCME(int mod_name) {
        try{
            mCompanyMaster companyById = companyService.getCompanyById(mod_name);
            List<mModulesMaster> allModule = companyById.getMModulesMasters();
            List<mModulesMaster> passNew= new ArrayList<>();
            for(mModulesMaster checked: allModule ) {
                mCompanyMaster companyMasterNew = checked.getCompanyMaster();
                if (companyMasterNew != null) {
                    mCompanyMaster fitnew=new mCompanyMaster();
                    fitnew.setCmpid(companyMasterNew.getCmpid());
                    fitnew.setCmpcode(companyMasterNew.getCmpcode());
                    fitnew.setCmpnm(companyMasterNew.getCmpnm());
                    fitnew.setClientMasters(null);
                    fitnew.setMModulesMasters(null);
                    fitnew.setIsactive(companyMasterNew.getIsactive());

                    checked.setCompanyMaster(fitnew);

                }else{
                    checked.setCompanyMaster(null);
                }

                    checked.setEmployeeMasterList(null);
                List<mClientCMEMaster> mClientCMEMasterList = checked.getMClientCMEMasterList();
                if(mClientCMEMasterList!=null){
                    List<mClientCMEMaster> fitnecclient= new ArrayList<>();
                    for( mClientCMEMaster cmeMaster: mClientCMEMasterList){
                        mClientCMEMaster pass = new mClientCMEMaster();
                        pass.setCmeId(cmeMaster.getCmeId());
                        pass.setCmeName(cmeMaster.getCmeName());
                        pass.setCmeDesignation(cmeMaster.getCmeDesignation());
                        pass.setCmeemailId(cmeMaster.getCmeemailId());
                        pass.setCmephoneNo(cmeMaster.getCmephoneNo());
                        mClientMaster clientMasterIdCme = cmeMaster.getClientMasterIdCme();
                        if (clientMasterIdCme!=null){
                            clientMasterIdCme.setCompanyMaster(null);
                            clientMasterIdCme.setMClientSPOCMasters(null);
                            clientMasterIdCme.setMClientCMEMasterList(null);
                        }
                        pass.setCmeId(cmeMaster.getCmeId());
                        fitnecclient.add(pass);

                    }
                    checked.setMClientCMEMasterList(fitnecclient);
                }
                passNew.add(checked);
            }
            return passNew;



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }









}
