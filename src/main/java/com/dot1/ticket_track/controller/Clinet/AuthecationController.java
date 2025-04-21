package com.dot1.ticket_track.controller.Clinet;

import com.dot1.ticket_track.dto.JwtAuthenticationResponcse;
import com.dot1.ticket_track.dto.RefreshTokenRequest;
import com.dot1.ticket_track.dto.SigninRequest;
import com.dot1.ticket_track.dto.SignupRequest;
import com.dot1.ticket_track.entity.mEmployeeMaster;
import com.dot1.ticket_track.entity.mUserLogin_demo;
import com.dot1.ticket_track.services.AuthenticationService;
import com.dot1.ticket_track.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthecationController {

    private final AuthenticationService  authenticationService;
    @Autowired
    private EmployeeService employeeService;


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest){
        try{
            mUserLogin_demo signup = authenticationService.signup(signupRequest);
            if(signup!=null){
                return new ResponseEntity<>(signup,HttpStatus.OK);
            }else{
                return  new ResponseEntity<>( HttpStatus.NOT_MODIFIED);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninRequest signinRequest){
        try{
            return ResponseEntity.ok(authenticationService.signin(signinRequest));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        try{
            return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
    @PostMapping("/addEmployee")
    @Transactional
    public ResponseEntity<?> addEmployee(@RequestBody mEmployeeMaster employee) {
        try {
            mEmployeeMaster adduserlogin = employeeService.addEmployee(employee);
            if(adduserlogin!=null){
                SignupRequest signupRequest = new SignupRequest();
                signupRequest.setUserID(adduserlogin.getEmailId());
                signupRequest.setUserPWD("Dot1@12345");
                signupRequest.setIsActLog(false);
                mUserLogin_demo signup = authenticationService.signup(signupRequest);
                if(signup !=null){
                    return new ResponseEntity<>(adduserlogin, HttpStatus.OK);

                }else{
                    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
                }
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/signupGet")
    public ResponseEntity<?> signupDirect(@RequestBody mUserLogin_demo signupRequest){
        try{
            return  new ResponseEntity<>(authenticationService.signupAdd(signupRequest), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/Getsignup")
    public ResponseEntity<?> Getsignup(){
        try{

            List<mUserLogin_demo> signup = authenticationService.getSignup();
            if(signup!=null){

                return new ResponseEntity<>(signup,HttpStatus.OK);

            }
else{
    return  new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }





}
