package com.dot1.ticket_track.entity;



import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mModuleMaster")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "modId")
public class mModulesMaster {

    @Id
    @Column(name = "modId", nullable = false,length = 9,unique = true)
    private Long modId;

    @Column(name = "modcode", nullable = false, length = 200, unique = true)
    private String modcode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mCompany_Master",nullable = false)
    private mCompanyMaster companyMaster;

    @OneToMany(mappedBy = "cmemodulesMaster", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<mClientCMEMaster> mClientCMEMasterList;

    @OneToMany(mappedBy = "modulesMaster_id", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<mEmployeeMaster> employeeMasterList;

    @Column(name = "isactive", columnDefinition = "TINYINT",nullable = false)
    private Boolean isactive;

}


