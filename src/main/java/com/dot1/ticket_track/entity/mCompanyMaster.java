package com.dot1.ticket_track.entity;


import com.fasterxml.jackson.annotation.*;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cmpid")
@Table(name = "mCompanyMaster")
public class mCompanyMaster {

    @Id
    @Column(name = "cmpid", nullable = false,length = 9,unique = true)
    private Integer cmpid;

    @Column(name = "cmpcode", nullable = false, length = 200, unique = true)
    private String cmpcode;

    @Column(name = "cmpnm", nullable = false, length = 400, unique = true)
    private String cmpnm;

    @Column(name = "isactive", columnDefinition = "TINYINT",nullable = false)
    private Boolean isactive;

    @Column(name = "insdate", nullable = true)
    @JsonFormat(pattern = "dd-MM-yyyy 'T' HH:mm:ss")
    private LocalDateTime insdate;


    @OneToMany(mappedBy = "companyMaster", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<mClientMaster> clientMasters=new ArrayList<>();



    @OneToMany(mappedBy = "companyMaster", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<mModulesMaster> mModulesMasters=new ArrayList<>();




    @PrePersist
    protected void onCreate() {
        insdate = LocalDateTime.now();

    }


}
