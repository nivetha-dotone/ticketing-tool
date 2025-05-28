package com.dot1.ticket_track.services;

import com.dot1.ticket_track.dto.JwtAuthenticationResponcse;
import com.dot1.ticket_track.dto.RefreshTokenRequest;
import com.dot1.ticket_track.dto.SigninRequest;
import com.dot1.ticket_track.dto.SignupRequest;
import com.dot1.ticket_track.entity.*;
import com.dot1.ticket_track.repository.AuthenticationserRepo;
import com.dot1.ticket_track.repository.EmployeeRepository;
import com.dot1.ticket_track.repository.UserLogin_demoRepo;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationserRepo {

    private final UserLogin_demoRepo userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private EmployeeRepository employeeRepository;


    public mUserLogin_demo signup(SignupRequest signupRequest){
       mUserLogin_demo user = new mUserLogin_demo();
       user.setLogId(userRepository.newIdUserLogin());
       user.setUserID(signupRequest.getUserID());
        mEmployeeMaster employeeMaster1 = employeeRepository.findById(signupRequest.getEmpId()).orElse(null);
        user.setEmp_Id(employeeMaster1);
        mEmployeeMaster employeeMaster = employeeRepository.findById(signupRequest.getEmpId()).orElse(null);
        if(employeeMaster!=null){
            String roleName = employeeMaster.getRoleMaster_id().getRoleName();
            user.setRole(roleName);
        }
        user.setUserPWD(passwordEncoder.encode(signupRequest.getUserPWD()));
        user.setIsactUser(signupRequest.getIsActLog());
        return  userRepository.save(user);
    }


    public mUserLogin_demo signupAdd(mUserLogin_demo signupRequest){
       mUserLogin_demo user = new mUserLogin_demo();
       user.setLogId(userRepository.newIdUserLogin());
       user.setUserID(signupRequest.getUserID());
       user.setEmp_Id(signupRequest.getEmp_Id());
        mEmployeeMaster empId1 = signupRequest.getEmp_Id();
        Integer empId = empId1.getEmpId();
        mEmployeeMaster employeeMaster = employeeRepository.findById(empId).orElse(null);
        if(employeeMaster!=null){

            String roleName = employeeMaster.getRoleMaster_id().getRoleName();
            user.setRole(roleName);
        }
        user.setUserPWD(passwordEncoder.encode(signupRequest.getUserPWD()));
        return  userRepository.save(user);

    }

    public List<mUserLogin_demo> getSignup(){

        List<mUserLogin_demo> all = userRepository.findAll();
        if(all!=null){
            for(mUserLogin_demo checkSign: all){
                String userPWD = checkSign.getUserPWD();
                mEmployeeMaster empId = checkSign.getEmp_Id();
                empId.setRoleMaster_id(null);
                empId.setModulesMaster_id(null);
                empId.setCompanyName(null);

            }
            return all;
        }else{
            return  null;
        }

    }

private final AuthenticationManager authenticationManager;
private  final JwtService jwtService;



    public JwtAuthenticationResponcse signin(SigninRequest signinRequest){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getUserID(),signinRequest.getUserPWD()));

            var user=userRepository.findByuserID(signinRequest.getUserID()).orElseThrow(()-> new IllegalStateException("Invalid UserID"));

            var jwt= jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(new HashMap<>(),user);

            JwtAuthenticationResponcse jwtAuthenticationResponcse = new JwtAuthenticationResponcse();

            jwtAuthenticationResponcse.setToken(jwt);
            jwtAuthenticationResponcse.setRefreshToken(refreshToken);
            jwtAuthenticationResponcse.setUserID(user.getUserID());
            jwtAuthenticationResponcse.setRole(user.getRole());
            jwtAuthenticationResponcse.setUserPassActive(user.getIsactUser());
            if(user.getEmp_Id()!=null){
                mEmployeeMaster empId = user.getEmp_Id();
                mModulesMaster modulesMasterId = empId.getModulesMaster_id();
                if(modulesMasterId!=null){
                    modulesMasterId.setCompanyMaster(null);
                    modulesMasterId.setEmployeeMasterList(null);
                    modulesMasterId.setMClientCMEMasterList(null);
                    empId.setModulesMaster_id(modulesMasterId);
                }else{
                    empId.setModulesMaster_id(null);
                }
                empId.getRoleMaster_id().setEmployeeMasterList(null);



                jwtAuthenticationResponcse.setEmployeeMaster(empId);
            }else{
                jwtAuthenticationResponcse.setEmployeeMaster(null);

            }
            if(user.getCmeMaster()!=null){

                mClientCMEMaster cmeMaster = user.getCmeMaster();
                mModulesMaster modulesMasterId = cmeMaster.getCmemodulesMaster();
                if(modulesMasterId!=null){

                    mCompanyMaster companyMaster = modulesMasterId.getCompanyMaster();
                    if(companyMaster!=null){
                        mCompanyMaster companyPass=new mCompanyMaster();
                        companyPass.setCmpid(companyMaster.getCmpid());
                        companyPass.setCmpcode(companyMaster.getCmpcode());
                        companyPass.setCmpnm(companyMaster.getCmpnm());
                        companyPass.setClientMasters(null);
                        companyPass.setMModulesMasters(null);
                        modulesMasterId.setCompanyMaster(companyPass);
                    }else{
                        modulesMasterId.setCompanyMaster(null);

                    }
                    modulesMasterId.setEmployeeMasterList(null);
                    modulesMasterId.setMClientCMEMasterList(null);
                    cmeMaster.setCmemodulesMaster(modulesMasterId);
                }else{
                    cmeMaster.setCmemodulesMaster(null);
                }
                mClientMaster clientMasterIdCme = cmeMaster.getClientMasterIdCme();
                if(clientMasterIdCme!=null){
                    clientMasterIdCme.setCompanyMaster(null);
                clientMasterIdCme.setMClientCMEMasterList(null);
                clientMasterIdCme.setMClientSPOCMasters(null);
                cmeMaster.setClientMasterIdCme(clientMasterIdCme);
                }else{
                    cmeMaster.setCmemodulesMaster(null);
                }


                jwtAuthenticationResponcse.setClientCMEMaster(cmeMaster);


            }else{
                jwtAuthenticationResponcse.setClientCMEMaster(null);

            }

            return jwtAuthenticationResponcse;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public JwtAuthenticationResponcse refreshToken (RefreshTokenRequest refreshTokenRequest){
        try {

            String userId= jwtService.extractUserName(refreshTokenRequest.getToken());
            mUserLogin_demo mUserLoginDemo = userRepository.findByuserID(userId).orElseThrow();
            if(jwtService.isTokenValid(refreshTokenRequest.getToken(), mUserLoginDemo )){
                var jwt= jwtService.generateToken(mUserLoginDemo);

                JwtAuthenticationResponcse jwtAuthenticationResponcse = new JwtAuthenticationResponcse();

                jwtAuthenticationResponcse.setToken(jwt);
                jwtAuthenticationResponcse.setRefreshToken(refreshTokenRequest.getToken());
                return jwtAuthenticationResponcse;

            }else{
                return null;
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }



}
