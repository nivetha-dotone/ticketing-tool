package com.dot1.ticket_track.repository;

import com.dot1.ticket_track.entity.mUserLogin_demo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

//import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface UserLogin_demoRepo extends JpaRepository<mUserLogin_demo, Long> {

    Optional<mUserLogin_demo> findByuserID(String email);


    List<mUserLogin_demo> findByrole(String role);

    @Query(value = "SELECT NEXT VALUE FOR UniqueSecUserLogin AS UniqueNumber",nativeQuery = true)
    Long newIdUserLogin();


//     CREATE SEQUENCE UniqueSecUserLogin
//     AS INT
//     START WITH 2100000
//     INCREMENT BY 1
//     NO CYCLE;
//     SELECT NEXT VALUE FOR UniqueSecUserLogin AS UniqueNumber;

}
