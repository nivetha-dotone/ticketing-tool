package com.dot1.ticket_track.services;

import com.dot1.ticket_track.entity.mUserLogin_demo;
import com.dot1.ticket_track.repository.EmployeeRepository;
import com.dot1.ticket_track.repository.RoleRepository;
import com.dot1.ticket_track.repository.UserLogin_demoRepo;
import com.dot1.ticket_track.webtoken.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLogin_demoService implements UserService {

  @Autowired
  private UserLogin_demoRepo userRepository;


  @Override
  public UserDetailsService userDetailsService(){
    try{
    return  new UserDetailsService() {
      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByuserID(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not found"));
      }
    };
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }




}
