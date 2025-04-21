package com.dot1.ticket_track.services;
import com.dot1.ticket_track.entity.*;
import com.dot1.ticket_track.repository.CompanyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;
    public mCompanyMaster createCompany(mCompanyMaster company) {
        try{
            if(company!=null){
                if(company.getIsactive()==null){
                    company.setIsactive(false);
                }
                company.setCmpid(companyRepository.newIdCMP());
                return companyRepository.save(company);
            }else{
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<mCompanyMaster> getAllCompanies() {
        List<mCompanyMaster>  fetchedALL = companyRepository.findAll();
        List<mCompanyMaster>  passedActive =new ArrayList<>();
        for(mCompanyMaster speratedIsActive : fetchedALL ){

                speratedIsActive.setClientMasters(null);
                List<mModulesMaster> mModulesMasters = speratedIsActive.getMModulesMasters();
                List<mModulesMaster> moduPass=new ArrayList<>();

                if(mModulesMasters!=null){
                    for(mModulesMaster checked: mModulesMasters){
                        checked.setEmployeeMasterList(null);
                        checked.setMClientCMEMasterList(null);
                        moduPass.add(checked);
                    }
                }
                speratedIsActive.setMModulesMasters(moduPass);


                passedActive.add(speratedIsActive);
            }

        if(passedActive!=null){
            return  passedActive;
        }else{
            return null;
        }
    }
    public List<mCompanyMaster> getAllActive() {
        List<mCompanyMaster>  fetchedALL = companyRepository.findAll();
        List<mCompanyMaster>  passedActive =new ArrayList<>();
        for(mCompanyMaster speratedIsActive : fetchedALL ){
            Boolean isactive = speratedIsActive.getIsactive();
            if(isactive!=null && isactive!=false){
                speratedIsActive.setClientMasters(null);
                List<mModulesMaster> mModulesMasters = speratedIsActive.getMModulesMasters();
                List<mModulesMaster> moduPass=new ArrayList<>();

                if(mModulesMasters!=null){
                    for(mModulesMaster checked: mModulesMasters){
                        checked.setEmployeeMasterList(null);
                        checked.setMClientCMEMasterList(null);
                        moduPass.add(checked);
                    }
                }
                speratedIsActive.setMModulesMasters(moduPass);


                passedActive.add(speratedIsActive);
            }

        }

        if(passedActive!=null){
            return  passedActive;
        }else{
            return null;
        }
    }
    public mCompanyMaster getCompanyById(Integer id) {
        mCompanyMaster mCompanyMaster = companyRepository.findById(id).orElse(null);
        if(mCompanyMaster!=null){
            mCompanyMaster.setClientMasters(null);
            List<mModulesMaster> mModulesMasters = mCompanyMaster.getMModulesMasters();
            List<mModulesMaster> moduPass=new ArrayList<>();

            if(mModulesMasters!=null){
                for(mModulesMaster checked: mModulesMasters){
                    checked.setEmployeeMasterList(null);
                    checked.setMClientCMEMasterList(null);
                    moduPass.add(checked);
                }
            }
            mCompanyMaster.setMModulesMasters(moduPass);



            return mCompanyMaster;
        }else{
            return null;
        }
    }
    public   List<mClientMaster> getCompanyByIdclient(Integer id) {
        mCompanyMaster mCompanyMaster = companyRepository.findById(id).orElse(null);
        if (mCompanyMaster != null) {
            List<mClientMaster> getAll = mCompanyMaster.getClientMasters();
            List<mClientMaster> passedAll = new ArrayList<>();
            for (mClientMaster checked : getAll) {
                mCompanyMaster companyMaster = checked.getCompanyMaster();
                if (companyMaster != null) {
                    mCompanyMaster fitnew = new mCompanyMaster();
                    fitnew.setCmpid(companyMaster.getCmpid());
                    fitnew.setCmpcode(companyMaster.getCmpcode());
                    fitnew.setCmpnm(companyMaster.getCmpnm());

                    checked.setCompanyMaster(fitnew);
                }
                List<mClientSPOCMaster> mClientSPOCMasters = checked.getMClientSPOCMasters();
                if (mClientSPOCMasters != null) {
                    List<mClientSPOCMaster> firnewspoc = new ArrayList<>();
                    for (mClientSPOCMaster checkspoc : mClientSPOCMasters) {
                        checkspoc.setClientmasterId(null);
                        firnewspoc.add(checkspoc);
                    }
                    checked.setMClientSPOCMasters(firnewspoc);
                }
                List<mClientCMEMaster> mClientCMEMasterList = checked.getMClientCMEMasterList();
                if (mClientCMEMasterList != null) {
                    List<mClientCMEMaster> fitnewcme = new ArrayList<>();
                    for (mClientCMEMaster checkedcme : mClientCMEMasterList) {
                        checkedcme.setClientMasterIdCme(null);
                        mModulesMaster cmemodulesMaster = checkedcme.getCmemodulesMaster();
                        if (cmemodulesMaster != null) {
                            mModulesMaster fitnewmod = new mModulesMaster();
                            fitnewmod.setModId(cmemodulesMaster.getModId());
                            fitnewmod.setModcode(cmemodulesMaster.getModcode());
                            fitnewmod.setIsactive(cmemodulesMaster.getIsactive());
                            fitnewmod.setCompanyMaster(null);
                            fitnewmod.setEmployeeMasterList(null);
                            fitnewmod.setMClientCMEMasterList(null);
                            checkedcme.setCmemodulesMaster(fitnewmod);
                        }
//                       checkedcme.setCmemodulesMaster(cmemodulesMaster);
                        fitnewcme.add(checkedcme);


                    }
                    checked.setMClientCMEMasterList(fitnewcme);
                }


                passedAll.add(checked);

                mCompanyMaster.setClientMasters(passedAll);
                mCompanyMaster.setMModulesMasters(null);
            }
            return passedAll;

        }
        else{
                return null;
            }

    }




    public Optional<mCompanyMaster> getCompanyby(Integer id) {
      return companyRepository.findById(id);
    }





    public mCompanyMaster updateCompany(Integer id, mCompanyMaster updatedCompany) {
//        return companyRepository.findById(id).map(company -> {
//            company.setCmpnm(updatedCompany.getCmpnm());
//            company.setCmpcode(updatedCompany.getCmpnm());
//            company.setIsActive(updatedCompany.getIsactive());
//
//            return companyRepository.save(company);
//        }).orElseThrow(() -> new RuntimeException("Company not found"));
       try {
           mCompanyMaster oldCompany = companyRepository.findById(id)
                   .orElseThrow(() -> new RuntimeException("Company not found with ID: " + id));
           oldCompany.setCmpcode(updatedCompany.getCmpcode());
           oldCompany.setCmpnm(updatedCompany.getCmpnm());
           oldCompany.setIsactive(updatedCompany.getIsactive());
            return companyRepository.save(oldCompany);

       } catch (Exception e) {
           throw new RuntimeException(e);
       }


    }
    public Boolean deleteCompany(Integer id) {
        mCompanyMaster mCompanyMaster = companyRepository.findById(id).orElse(null);
        if(mCompanyMaster!=null){
            companyRepository.deleteById(id);
            System.out.println("Successfully deleted");
            return true;
        }else{
            System.out.println("NOT FOUND");
            return false;
        }
    }
    @Transactional
    public mCompanyMaster updateCompanyIsActive(Integer companyId, Boolean isActive) {
        try{
        mCompanyMaster company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));
        company.setIsactive(isActive);
        return companyRepository.save(company);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}