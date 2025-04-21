package com.dot1.ticket_track.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "uatTranscation")
public class uatTranscation {

    @Id
    @Column(name = "uatId", nullable = false,length = 9,unique = true)
    private Long uatId;

    @Column(name = "comment", nullable = false,length = 2000)
    private String comment;

    @JoinColumn(name = "employeeId", referencedColumnName = "empId")
    @ManyToOne(fetch = FetchType.EAGER)
    private mEmployeeMaster employeeId;

    @JoinColumn(name = "cmexpertId", referencedColumnName = "cmeId")
    @ManyToOne(fetch = FetchType.EAGER)
    private mClientCMEMaster cmexpertId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status")
    private mGeneralMaster status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sub_status")
    private mTicketSubStatus sub_status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_number")
    private mTicketSdeatils ticket_number;

    @Column(name = "insdate", nullable = true, updatable = false)
    @JsonFormat(pattern = "dd-MM-yyyy 'T' HH:mm:ss")
    private LocalDateTime insertdtmUat;

    @PrePersist
    protected void onCreate() {
        this.insertdtmUat = LocalDateTime.now();
    }





}
