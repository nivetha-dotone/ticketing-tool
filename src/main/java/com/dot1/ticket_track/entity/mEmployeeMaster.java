package com.dot1.ticket_track.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mEmployeeMaster")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "empId")
public class mEmployeeMaster {

    @Id
    @Column(name = "empId",nullable = false)
    private int empId;

//    @Column(name = "managerId")
//    private Integer managerId;

    @Column(name = "empCode", nullable = false, length = 10, unique = true)
    private String empCode;

    @Column(name = "empName", nullable = false, length = 100)
    private String empName;

//    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    @JoinColumn(name = "cmpId", referencedColumnName = "cmpId", nullable = true)
//    @JoinTable(name="cmp_emp",
//            joinColumns = {@JoinColumn(name = "emp_id")},
//            inverseJoinColumns ={
//                    @JoinColumn(name="cmp_id")
//            } )
//    private mCompanyMaster cmpId;


    @Column(name = "emailId", unique = true, length = 200)
    private String emailId;

   @Column(name = "phoneNo",unique = true, length = 200)
    private String phoneNo;

    @Column(name = "companyName", length = 200)
    private String CompanyName;

    @Column(name = "dateOfJoining")
    @JsonFormat(pattern = "yyyy-MM-dd 'T' HH:mm:ss")
    private LocalDateTime dateOfJoining;

    @Column(name = "isactive", columnDefinition = "TINYINT",nullable = true)
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Rolemaster_id",nullable = false)
    private mRoleMaster roleMaster_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "modulesMaster_id",nullable = true)
    private mModulesMaster modulesMaster_id;

    @Column(name = "insertDtm", nullable = false, updatable = false)
    @JsonFormat(pattern = "dd-MM-yyyy 'T' HH:mm:ss")
    private LocalDateTime insertDtm;

    @Column(name = "updateDtm", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy 'T' HH:mm:ss")
    private LocalDateTime updateDtm;

    // Lifecycle Callbacks
    @PrePersist
    protected void onCreate() {
        insertDtm = LocalDateTime.now();
        updateDtm = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateDtm = LocalDateTime.now();
    }

    public void setIsActive(Boolean isActive) {
        // Ensure the client cannot be active if the company is not active
        if (roleMaster_id != null && !roleMaster_id.getIsActive() && isActive) {
            throw new IllegalStateException("Cannot activate Employee when the parent Role is inactive.");
        }
        this.isActive = isActive;
    }

}
