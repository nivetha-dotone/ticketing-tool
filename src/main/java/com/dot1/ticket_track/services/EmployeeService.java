package com.dot1.ticket_track.services;

import com.dot1.ticket_track.entity.*;
import com.dot1.ticket_track.repository.EmployeeRepository;
import com.dot1.ticket_track.repository.UserLogin_demoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmployeeRepository employeeRepository;
@Autowired
private UserLogin_demoRepo userLoginDemoRepo;

    public mEmployeeMaster addEmployee(mEmployeeMaster employee) {
        try {



            if(employee!=null){

                employee.setEmpId(employeeRepository.newempID());
                mEmployeeMaster saved = employeeRepository.save(employee);
                mUserLogin_demo save = adduserlogin(saved);


                return saved;
            }else{
                return null;
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public mUserLogin_demo adduserlogin(mEmployeeMaster employee) {
        try {

            mUserLogin_demo mUserLoginDemo = new mUserLogin_demo();
            mUserLoginDemo.setLogId(userLoginDemoRepo.newIdUserLogin());
            mUserLoginDemo.setUserID(employee.getEmailId());
            mUserLoginDemo.setUserPWD(passwordEncoder.encode("Dot1@12345"));
            mUserLoginDemo.setEmp_Id(employee);
            mUserLoginDemo.setIsactUser(false);
            mUserLoginDemo.setRole(employee.getRoleMaster_id().getRoleName());
            mUserLogin_demo save = userLoginDemoRepo.save(mUserLoginDemo);
            if(save!=null){


               return save;
            }else{
                return  null;
            }





        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public List<mEmployeeMaster> getAllEmployees() {


        List<mEmployeeMaster> AllEmployee = employeeRepository.findAll();

        if(AllEmployee!=null){
            List<mEmployeeMaster> Passed = new ArrayList<>();
            for(mEmployeeMaster checked: AllEmployee){
                mEmployeeMaster fitnew =new mEmployeeMaster();
                fitnew.setEmpId(checked.getEmpId());
                fitnew.setEmpCode(checked.getEmpCode());
                fitnew.setEmpName(checked.getEmpName());
                fitnew.setEmailId(checked.getEmailId());
                fitnew.setPhoneNo(checked.getPhoneNo());
                fitnew.setDateOfJoining(checked.getDateOfJoining());
                fitnew.setIsActive(checked.getIsActive());
                mRoleMaster roleMasterId = checked.getRoleMaster_id();
                if(roleMasterId!=null){
                    roleMasterId.setEmployeeMasterList(null);
                    fitnew.setRoleMaster_id(roleMasterId);
                }
                fitnew.setCompanyName(checked.getCompanyName());
                mModulesMaster modulesMasterId = checked.getModulesMaster_id();
                if(modulesMasterId!=null){
                    mModulesMaster fitnewmod= new mModulesMaster();
                    fitnewmod.setModId(modulesMasterId.getModId());
                    fitnewmod.setModcode(modulesMasterId.getModcode());
                    fitnewmod.setIsactive(modulesMasterId.getIsactive());
                    mCompanyMaster companyMaster = modulesMasterId.getCompanyMaster();
                    if(companyMaster!=null){
                        mCompanyMaster fitcom=new mCompanyMaster();
                        fitcom.setCmpid(companyMaster.getCmpid());
                        fitcom.setCmpcode(companyMaster.getCmpcode());
                        fitcom.setCmpnm(companyMaster.getCmpnm());
                        fitcom.setIsactive(companyMaster.getIsactive());
                        fitcom.setInsdate(companyMaster.getInsdate());
                        fitcom.setMModulesMasters(null);
                        fitnewmod.setCompanyMaster(fitcom);

                    }
                    fitnewmod.setEmployeeMasterList(null);
                    fitnew.setModulesMaster_id(fitnewmod);
                }
                fitnew.setInsertDtm(checked.getInsertDtm());
                fitnew.setUpdateDtm(checked.getUpdateDtm());
                Passed.add(fitnew);
            }
            return Passed;
        }else{
            return null;
        }


    }

    public mEmployeeMaster getEmployeeById(int empId) {
        mEmployeeMaster checked = employeeRepository.findById(empId).orElse(null);
        mEmployeeMaster fitnew =new mEmployeeMaster();
        if(checked!=null){
            fitnew.setEmpId(checked.getEmpId());
            fitnew.setEmpCode(checked.getEmpCode());
            fitnew.setEmpName(checked.getEmpName());
            fitnew.setEmailId(checked.getEmailId());
            fitnew.setPhoneNo(checked.getPhoneNo());
            fitnew.setDateOfJoining(checked.getDateOfJoining());
            fitnew.setIsActive(checked.getIsActive());
            mRoleMaster roleMasterId = checked.getRoleMaster_id();
            if(roleMasterId!=null){
                roleMasterId.setEmployeeMasterList(null);
                fitnew.setRoleMaster_id(roleMasterId);
            }
            fitnew.setCompanyName(checked.getCompanyName());
            mModulesMaster modulesMasterId = checked.getModulesMaster_id();
            if(modulesMasterId!=null){
                mModulesMaster fitnewmod= new mModulesMaster();
                fitnewmod.setModId(modulesMasterId.getModId());
                fitnewmod.setModcode(modulesMasterId.getModcode());
                fitnewmod.setIsactive(modulesMasterId.getIsactive());
                mCompanyMaster companyMaster = modulesMasterId.getCompanyMaster();
                if(companyMaster!=null){
                    mCompanyMaster fitcom=new mCompanyMaster();
                    fitcom.setCmpid(companyMaster.getCmpid());
                    fitcom.setCmpcode(companyMaster.getCmpcode());
                    fitcom.setCmpnm(companyMaster.getCmpnm());
                    fitcom.setIsactive(companyMaster.getIsactive());
                    fitcom.setInsdate(companyMaster.getInsdate());
                    fitcom.setMModulesMasters(null);
                    fitnewmod.setCompanyMaster(fitcom);

                }
                fitnewmod.setEmployeeMasterList(null);
                fitnew.setModulesMaster_id(fitnewmod);
            }
            fitnew.setInsertDtm(checked.getInsertDtm());
            fitnew.setUpdateDtm(checked.getUpdateDtm());
            return fitnew;
        }else{
            return null;
        }


    }

     public mEmployeeMaster getByempCode(String empCode) {
        mEmployeeMaster checked = employeeRepository.findByempCode(empCode).orElse(null);
        mEmployeeMaster fitnew =new mEmployeeMaster();
        if(checked!=null){
            fitnew.setEmpId(checked.getEmpId());
            fitnew.setEmpCode(checked.getEmpCode());
            fitnew.setEmpName(checked.getEmpName());
            fitnew.setEmailId(checked.getEmailId());
            fitnew.setPhoneNo(checked.getPhoneNo());
            fitnew.setDateOfJoining(checked.getDateOfJoining());
            fitnew.setIsActive(checked.getIsActive());
            mRoleMaster roleMasterId = checked.getRoleMaster_id();
            if(roleMasterId!=null){
                roleMasterId.setEmployeeMasterList(null);
                fitnew.setRoleMaster_id(roleMasterId);
            }
            fitnew.setCompanyName(checked.getCompanyName());
            mModulesMaster modulesMasterId = checked.getModulesMaster_id();
            if(modulesMasterId!=null){
                mModulesMaster fitnewmod= new mModulesMaster();
                fitnewmod.setModId(modulesMasterId.getModId());
                fitnewmod.setModcode(modulesMasterId.getModcode());
                fitnewmod.setIsactive(modulesMasterId.getIsactive());
                mCompanyMaster companyMaster = modulesMasterId.getCompanyMaster();
                if(companyMaster!=null){
                    mCompanyMaster fitcom=new mCompanyMaster();
                    fitcom.setCmpid(companyMaster.getCmpid());
                    fitcom.setCmpcode(companyMaster.getCmpcode());
                    fitcom.setCmpnm(companyMaster.getCmpnm());
                    fitcom.setIsactive(companyMaster.getIsactive());
                    fitcom.setInsdate(companyMaster.getInsdate());
                    fitcom.setMModulesMasters(null);
                    fitnewmod.setCompanyMaster(fitcom);

                }
                fitnewmod.setEmployeeMasterList(null);
                fitnew.setModulesMaster_id(fitnewmod);
            }
            fitnew.setInsertDtm(checked.getInsertDtm());
            fitnew.setUpdateDtm(checked.getUpdateDtm());
            return fitnew;
        }else{
            return null;
        }


    }


 public List<mEmployeeMaster> getByempName(String empName) {
     List<mEmployeeMaster> AllEmployee = employeeRepository.findByempName(empName).orElse(null);

     if(AllEmployee!=null){
         List<mEmployeeMaster> Passed = new ArrayList<>();
         for(mEmployeeMaster checked: AllEmployee){
             mEmployeeMaster fitnew =new mEmployeeMaster();
             fitnew.setEmpId(checked.getEmpId());
             fitnew.setEmpCode(checked.getEmpCode());
             fitnew.setEmpName(checked.getEmpName());
             fitnew.setEmailId(checked.getEmailId());
             fitnew.setPhoneNo(checked.getPhoneNo());
             fitnew.setDateOfJoining(checked.getDateOfJoining());
             fitnew.setIsActive(checked.getIsActive());
             mRoleMaster roleMasterId = checked.getRoleMaster_id();
             if(roleMasterId!=null){
                 roleMasterId.setEmployeeMasterList(null);
                 fitnew.setRoleMaster_id(roleMasterId);
             }
             fitnew.setCompanyName(checked.getCompanyName());
             mModulesMaster modulesMasterId = checked.getModulesMaster_id();
             if(modulesMasterId!=null){
                 mModulesMaster fitnewmod= new mModulesMaster();
                 fitnewmod.setModId(modulesMasterId.getModId());
                 fitnewmod.setModcode(modulesMasterId.getModcode());
                 fitnewmod.setIsactive(modulesMasterId.getIsactive());
                 mCompanyMaster companyMaster = modulesMasterId.getCompanyMaster();
                 if(companyMaster!=null){
                     mCompanyMaster fitcom=new mCompanyMaster();
                     fitcom.setCmpid(companyMaster.getCmpid());
                     fitcom.setCmpcode(companyMaster.getCmpcode());
                     fitcom.setCmpnm(companyMaster.getCmpnm());
                     fitcom.setIsactive(companyMaster.getIsactive());
                     fitcom.setInsdate(companyMaster.getInsdate());
                     fitcom.setMModulesMasters(null);
                     fitnewmod.setCompanyMaster(fitcom);

                 }
                 fitnewmod.setEmployeeMasterList(null);
                 fitnew.setModulesMaster_id(fitnewmod);
             }
             fitnew.setInsertDtm(checked.getInsertDtm());
             fitnew.setUpdateDtm(checked.getUpdateDtm());
             Passed.add(fitnew);
         }
         return Passed;
     }else{
         return null;
     }
    }

//    @Autowired
//    private ModuleServices moduleServices;
//
//    public List<mEmployeeMaster> getByModID(Integer id) {
//        mModulesMaster modulemas = moduleServices.modulesMasterforEmployee(id);
//        List<mEmployeeMaster> AllEmployee = employeeRepository.findBymodulesMaster_id(modulemas).orElse(null);
//     if(AllEmployee!=null){
//         List<mEmployeeMaster> Passed = new ArrayList<>();
//         for(mEmployeeMaster checked: AllEmployee){
//             mEmployeeMaster fitnew =new mEmployeeMaster();
//             fitnew.setEmpId(checked.getEmpId());
//             fitnew.setEmpCode(checked.getEmpCode());
//             fitnew.setEmpName(checked.getEmpName());
//             fitnew.setEmailId(checked.getEmailId());
//             fitnew.setPhoneNo(checked.getPhoneNo());
//             fitnew.setDateOfJoining(checked.getDateOfJoining());
//             fitnew.setIsActive(checked.getIsActive());
//             mRoleMaster roleMasterId = checked.getRoleMaster_id();
//             if(roleMasterId!=null){
//                 roleMasterId.setEmployeeMasterList(null);
//                 fitnew.setRoleMaster_id(roleMasterId);
//             }
//             fitnew.setCompanyName(checked.getCompanyName());
//             mModulesMaster modulesMasterId = checked.getModulesMaster_id();
//             if(modulesMasterId!=null){
//                 mModulesMaster fitnewmod= new mModulesMaster();
//                 fitnewmod.setModId(modulesMasterId.getModId());
//                 fitnewmod.setModcode(modulesMasterId.getModcode());
//                 fitnewmod.setIsactive(modulesMasterId.getIsactive());
//                 mCompanyMaster companyMaster = modulesMasterId.getCompanyMaster();
//                 if(companyMaster!=null){
//                     mCompanyMaster fitcom=new mCompanyMaster();
//                     fitcom.setCmpid(companyMaster.getCmpid());
//                     fitcom.setCmpcode(companyMaster.getCmpcode());
//                     fitcom.setCmpnm(companyMaster.getCmpnm());
//                     fitcom.setIsActive(companyMaster.getIsactive());
//                     fitcom.setInsdate(companyMaster.getInsdate());
//                     fitcom.setMModulesMasters(null);
//                     fitnewmod.setCompanyMaster(fitcom);
//
//                 }
//                 fitnewmod.setEmployeeMasterList(null);
//                 fitnew.setModulesMaster_id(fitnewmod);
//             }
//             fitnew.setInsertDtm(checked.getInsertDtm());
//             fitnew.setUpdateDtm(checked.getUpdateDtm());
//             Passed.add(fitnew);
//         }
//         return Passed;
//     }else{
//         return null;
//     }
//    }
//



    public mEmployeeMaster updateEmployee(int empId, mEmployeeMaster updatedEmployee) {
        return employeeRepository.findById(empId).map(employee -> {
            employee.setEmpName(updatedEmployee.getEmpName());
            employee.setEmpCode(updatedEmployee.getEmpCode());
//            employee.setc(updatedEmployee.getCmpId());
            employee.setRoleMaster_id(updatedEmployee.getRoleMaster_id());
            employee.setEmailId(updatedEmployee.getEmailId());
            employee.setDateOfJoining(updatedEmployee.getDateOfJoining());
            employee.setIsActive(updatedEmployee.getIsActive());
            return employeeRepository.save(employee);
        }).orElse(null);
    }

    public boolean deleteEmployee(int empId) {
        if (employeeRepository.existsById(empId)) {
            employeeRepository.deleteById(empId);
            return true;
        }
        return false;
    }
}
