package com.dot1.ticket_track.services;

import com.dot1.ticket_track.entity.mEmployeeMaster;
import com.dot1.ticket_track.entity.mModulesMaster;
import com.dot1.ticket_track.entity.mRoleMaster;
import com.dot1.ticket_track.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleMasterService {

    @Autowired
    private RoleRepository roleRepository;

    public mRoleMaster add(mRoleMaster roleMaster){
    try{
        mRoleMaster save = roleRepository.save(roleMaster);
        if(save!=null){
            return save;

        }else{
            return null;
        }
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    }


    public List<mRoleMaster> getAll(){
        try{
            List<mRoleMaster> fetchedAll = roleRepository.findAll();
            List<mRoleMaster> passedActive = new ArrayList<>();
            for(mRoleMaster speratedIsActive : fetchedAll ) {
                    mRoleMaster passedRole=new mRoleMaster();
                    if(speratedIsActive!=null)
                    {
                        passedRole.setRoleId(speratedIsActive.getRoleId());
                        passedRole.setRoleName(speratedIsActive.getRoleName());
                        passedRole.setRoleDescription(speratedIsActive.getRoleDescription());
                        passedRole.setIsActive(speratedIsActive.getIsActive());
                        List<mEmployeeMaster> employeeMasterList = speratedIsActive.getEmployeeMasterList();
                        List<mEmployeeMaster> fitnewList=new ArrayList<>();
                        if(employeeMasterList!=null){
                            for(mEmployeeMaster employeeMaster: employeeMasterList){
//                                employeeMaster.setRoleMaster_id(null);
                                mEmployeeMaster passedemp=new mEmployeeMaster();
                                passedemp.setRoleMaster_id(null);
                                passedemp.setEmpId(employeeMaster.getEmpId());
                                passedemp.setEmpName(employeeMaster.getEmpName());
                                passedemp.setEmpCode(employeeMaster.getEmpCode());
                                passedemp.setEmailId(employeeMaster.getEmailId());
                                passedemp.setPhoneNo(employeeMaster.getPhoneNo());
                                passedemp.setIsActive(employeeMaster.getIsActive());
                                passedemp.setCompanyName(employeeMaster.getCompanyName());
                                passedemp.setDateOfJoining(employeeMaster.getDateOfJoining());
                                mModulesMaster modulesMasterId = employeeMaster.getModulesMaster_id();
                                if(modulesMasterId!=null) {
                                    mModulesMaster firnewmodule=new mModulesMaster();
                                    firnewmodule.setEmployeeMasterList(null);
                                    firnewmodule.setCompanyMaster(null);
                                    firnewmodule.setMClientCMEMasterList(null);
                                    firnewmodule.setModId(modulesMasterId.getModId());
                                    firnewmodule.setModcode(modulesMasterId.getModcode());
                                    firnewmodule.setIsactive(modulesMasterId.getIsactive());
                                    passedemp.setModulesMaster_id(firnewmodule);
                                }
//                                passedemp.setModulesMaster_id(modulesMasterId);
                                passedemp.setRoleMaster_id(null);

                                fitnewList.add(passedemp);

//                                fitnewList.add(employeeMaster);

                            }
                        }
                        passedRole.setEmployeeMasterList(fitnewList);
                        passedActive.add(passedRole);
                    }

            }
            if(passedActive!=null){
                return passedActive;

            }else{
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<mRoleMaster> getAllActive(){
    try{
        List<mRoleMaster> fetchedAll = roleRepository.findAll();
        List<mRoleMaster> passedActive = new ArrayList<>();
        for(mRoleMaster speratedIsActive : fetchedAll ) {
            Boolean isactive = speratedIsActive.getIsActive();
            if (isactive != null && isactive == true) {

                mRoleMaster passedRole=new mRoleMaster();
                if(speratedIsActive!=null)
                {
                    passedRole.setRoleId(speratedIsActive.getRoleId());
                    passedRole.setRoleName(speratedIsActive.getRoleName());
                    passedRole.setRoleDescription(speratedIsActive.getRoleDescription());
                    passedRole.setIsActive(speratedIsActive.getIsActive());
                    List<mEmployeeMaster> employeeMasterList = speratedIsActive.getEmployeeMasterList();
                    List<mEmployeeMaster> fitnewList=new ArrayList<>();
                    if(employeeMasterList!=null){
                        for(mEmployeeMaster employeeMaster: employeeMasterList){
//                            employeeMaster.setRoleMaster_id(null);
                            mEmployeeMaster passedemp=new mEmployeeMaster();
                            passedemp.setRoleMaster_id(null);
                            passedemp.setEmpId(employeeMaster.getEmpId());
                            passedemp.setEmpCode(employeeMaster.getEmpCode());
                            passedemp.setEmpName(employeeMaster.getEmpName());
                            passedemp.setEmailId(employeeMaster.getEmailId());
                            passedemp.setPhoneNo(employeeMaster.getPhoneNo());
                            passedemp.setIsActive(employeeMaster.getIsActive());
                            passedemp.setCompanyName(employeeMaster.getCompanyName());
                            passedemp.setDateOfJoining(employeeMaster.getDateOfJoining());
                            mModulesMaster modulesMasterId = employeeMaster.getModulesMaster_id();
                            if(modulesMasterId!=null) {
                                mModulesMaster firnewmodule=new mModulesMaster();
                                firnewmodule.setEmployeeMasterList(null);
                                firnewmodule.setCompanyMaster(null);
                                firnewmodule.setMClientCMEMasterList(null);
                                firnewmodule.setModId(modulesMasterId.getModId());
                                firnewmodule.setModcode(modulesMasterId.getModcode());
                                firnewmodule.setIsactive(modulesMasterId.getIsactive());
                                passedemp.setModulesMaster_id(firnewmodule);
                            }
//                            passedemp.setModulesMaster_id(modulesMasterId);
                            passedemp.setRoleMaster_id(null);

                            fitnewList.add(passedemp);


//                            fitnewList.add(employeeMaster);

                        }
                    }
                    passedRole.setEmployeeMasterList(fitnewList);
                    passedActive.add(passedRole);
                }
            }
        }
        if(passedActive!=null){
            return passedActive;

        }else{
            return null;
        }
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    }

    public mRoleMaster getByroleName(String roleName){
    try{
        mRoleMaster fetchedRole = roleRepository.findByroleName(roleName).orElse(null);
        mRoleMaster passedRole=new mRoleMaster();
        if(fetchedRole!=null)
        {
            passedRole.setRoleId(fetchedRole.getRoleId());
            passedRole.setRoleName(fetchedRole.getRoleName());
            passedRole.setRoleDescription(fetchedRole.getRoleDescription());
            passedRole.setIsActive(fetchedRole.getIsActive());
            List<mEmployeeMaster> employeeMasterList = fetchedRole.getEmployeeMasterList();
            List<mEmployeeMaster> fitnewList=new ArrayList<>();
            if(employeeMasterList!=null){
                for(mEmployeeMaster employeeMaster: employeeMasterList){
                    mEmployeeMaster passedemp=new mEmployeeMaster();
                    passedemp.setRoleMaster_id(null);
                    passedemp.setEmpId(employeeMaster.getEmpId());
                    passedemp.setEmpCode(employeeMaster.getEmpCode());
                    passedemp.setEmpName(employeeMaster.getEmpName());
                    passedemp.setEmailId(employeeMaster.getEmailId());
                    passedemp.setPhoneNo(employeeMaster.getPhoneNo());
                    passedemp.setIsActive(employeeMaster.getIsActive());
                    passedemp.setCompanyName(employeeMaster.getCompanyName());
                    passedemp.setDateOfJoining(employeeMaster.getDateOfJoining());
                    mModulesMaster modulesMasterId = employeeMaster.getModulesMaster_id();
                    if(modulesMasterId!=null) {
                        mModulesMaster firnewmodule=new mModulesMaster();
                        firnewmodule.setEmployeeMasterList(null);
                        firnewmodule.setCompanyMaster(null);
                        firnewmodule.setMClientCMEMasterList(null);
                        firnewmodule.setModId(modulesMasterId.getModId());
                        firnewmodule.setModcode(modulesMasterId.getModcode());
                        firnewmodule.setIsactive(modulesMasterId.getIsactive());
                        passedemp.setModulesMaster_id(firnewmodule);
                    }
//                    passedemp.setModulesMaster_id(modulesMasterId);

                    passedemp.setRoleMaster_id(null);

                            fitnewList.add(passedemp);

                }
            }
            passedRole.setEmployeeMasterList(fitnewList);
return passedRole;
        }else{
            return null;
        }

    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    }


    public mRoleMaster updatedbyID(Integer roleId,mRoleMaster oldRoleMaster){
    try{
        mRoleMaster mRoleMaster = roleRepository.findById(roleId).orElse(null);
        if(mRoleMaster!=null){
            return mRoleMaster;
        }else {
            return null;
        }

    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    }

    public Boolean deletebyID(String roleId){
    try{
        mRoleMaster byid = getByroleName(roleId);
        if(byid!=null){
            roleRepository.delete(byid);
            return true;

        }else{
            return false;

        }

    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    }

    @Transactional
    public mRoleMaster updateRoleIsActive(Integer roleID, Boolean isActive) {

        try{

            mRoleMaster byid = roleRepository.findById(roleID)
                    .orElseThrow(() -> new RuntimeException("Company not found with ID: " + roleID));

            byid.setIsActive(isActive);
            return roleRepository.save(byid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
