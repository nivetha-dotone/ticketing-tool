package com.dot1.ticket_track.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mGeneralMaster")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "gmid")
public class mGeneralMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gmid", nullable = false, length = 4)
    private Integer gmid;

    @Column(name = "gmType", nullable = false, length = 20)
    private String gmType;

    @Column(name = "gmDescription", nullable = false, length = 200)
    private String gmDescription;

    @Column(name = "isActive", columnDefinition = "TINYINT")
    private Boolean isActive;

    @Column(name = "insertDtm", updatable = false)
    @JsonFormat(pattern = "dd-MM-yyyy 'T' HH:mm:ss")
    private LocalDateTime insertDtm;

    @JsonFormat(pattern = "dd-MM-yyyy 'T' HH:mm:ss")
    @Column(name = "updatedtm", nullable = true)
    private LocalDateTime updateDtm;

    @PrePersist
    protected void onCreate() {
        insertDtm = LocalDateTime.now();
        updateDtm = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateDtm = LocalDateTime.now();
    }
}
