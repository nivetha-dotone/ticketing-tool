package com.dot1.ticket_track.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_client_master")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "clientId")
public class mClientMaster {

    @Id
    @Column(name = "clientId", nullable = false,length = 9,unique = true)
    private Integer clientId;

    @Column(name = "clientCode",unique = true, nullable = false, length = 40)
    private String clientCode;

    @Column(name = "clientName", nullable = false, length = 200)
    private String clientName;

    @Column(name = "clientGroup", nullable = false, length = 200)
    private String clientGroup;

    @Column(name = "clientModule", nullable = false, length = -1)
    private String clientModule;

    @Column(name = "employeePriorityclient", length = 200)
    private  String employeeClient;

    @Column(name = "isactive", columnDefinition = "TINYINT",nullable = false)
    private Boolean isactive;

    @Column(name = "insertdtm", nullable = true)
    @JsonFormat(pattern = "dd-MM-yyyy 'T' HH:mm:ss")
    private LocalDateTime insertdtm;

    @JsonFormat(pattern = "dd-MM-yyyy 'T' HH:mm:ss")
    @Column(name = "updatedtm", nullable = true)
    private LocalDateTime updatedtm;


    // Foreign key relationship with mCompanyMaster table
 // @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mCompanyMaster_cmpid",nullable = false)
    private mCompanyMaster companyMaster;



    @OneToMany(mappedBy = "clientmasterId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<mClientSPOCMaster> mClientSPOCMasters;

    @OneToMany(mappedBy = "clientMasterIdCme", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<mClientCMEMaster> mClientCMEMasterList;

    @PrePersist
    protected void onCreate() {
        insertdtm = LocalDateTime.now();
        updatedtm = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedtm = LocalDateTime.now();
    }







}
    