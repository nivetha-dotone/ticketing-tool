package com.dot1.ticket_track.repository;

import com.dot1.ticket_track.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<mEmployeeMaster, Integer> {
    Optional<List<mEmployeeMaster>> findByempName(String empName);
    Optional<mEmployeeMaster> findByempCode(String empCode);

    @Query(value = "SELECT NEXT VALUE FOR UniqueSecEmp AS UniqueNumber",nativeQuery = true)
    Integer newempID();
}



//CREATE SEQUENCE UniqueSecEmp
//AS INT
//START WITH 200000
//INCREMENT BY 1
//NO CYCLE;

//SELECT NEXT VAsLUE FOR UniqueSecEmp AS UniqueNumber;