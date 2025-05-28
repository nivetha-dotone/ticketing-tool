package com.dot1.ticket_track.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mClientSPOCMaster")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class mClientSPOCMaster {

    @Id
    @Column(name = "spocId", nullable = false,length = 9,unique = true)
    private Integer spocId;

    @Column(name = "spocName", nullable = false, length = 200)
    private String spocName;

    @Column(name = "designation", nullable = false, length = 200)
    private String designation;

    @Column(name = "emailId", nullable = false, length = 200)
    private String emailId;

    @Column(name = "contactNumber", nullable = false, length = 200)
    private String contactNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "clientMasterID",nullable = false)
    private mClientMaster clientmasterId;

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
        if (clientmasterId != null && !clientmasterId.getIsactive() && isActive) {
            throw new IllegalStateException("Cannot activate clientSPOC when the parent client is inactive.");
        }
        this.isActive = isActive;
    }

}

