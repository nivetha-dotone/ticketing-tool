package com.dot1.ticket_track.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mRoleMaster")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class mRoleMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roleId")
    private Integer roleId;

    @Column(name = "roleName", nullable = false, length = 40, unique = true)
    private String roleName;

    @Column(name = "roleDescription", length = 2000)
    private String roleDescription;

    @Column(name = "isactive", columnDefinition = "TINYINT",nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "roleMaster_id", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<mEmployeeMaster> employeeMasterList=new ArrayList<>();

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

    // Setter to update `isActive` and propagate changes to clients

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
        if (!isActive) {
            // If the company is not active, set all clients to inactive
            for (mEmployeeMaster client : employeeMasterList) {

                client.setIsActive(false);
            }
        }

    }
}


