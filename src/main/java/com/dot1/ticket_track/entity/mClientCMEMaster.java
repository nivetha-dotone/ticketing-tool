package com.dot1.ticket_track.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mClientCMEMaster")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cmeId")
public class mClientCMEMaster {

    @Id
    @Column(name = "cmeId", nullable = false)
    private Integer cmeId;

    @Column(name = "cmeName", nullable = false, length = 200)
    private String cmeName;

    @Column(name = "cmeDesignation", nullable = false, length = 200)
    private String cmeDesignation;

    @Column(name = "cmeemailId", nullable = false, length = 200)
    private String cmeemailId;

    @Column(name = "cmephoneNo", nullable = false, length = 200)
    private String cmephoneNo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "clientMasterIdCme",nullable = false)
    private mClientMaster clientMasterIdCme;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cmemodulesMaster",nullable = false)
    private mModulesMaster cmemodulesMaster;

    @Column(name = "isActive", columnDefinition = "TINYINT",nullable = false)
    private Boolean isActive;

    @Column(name = "insertdtm", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy 'T' HH:mm:ss")
    private LocalDateTime insertdtm;

    @JsonFormat(pattern = "dd-MM-yyyy 'T' HH:mm:ss")
    @Column(name = "updatedtm", nullable = false)
    private LocalDateTime updatedtm;


    @PrePersist
    protected void onCreate() {
        insertdtm = LocalDateTime.now();
        updatedtm = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedtm = LocalDateTime.now();
    }


    public void setIsActive(Boolean isActive) {
        // Ensure the client cannot be active if the company is not active
        if (clientMasterIdCme != null && !clientMasterIdCme.getIsactive() && isActive) {
            throw new IllegalStateException("Cannot activate clientSPOC when the parent client is inactive.");
        }
        this.isActive = isActive;
    }

}

















