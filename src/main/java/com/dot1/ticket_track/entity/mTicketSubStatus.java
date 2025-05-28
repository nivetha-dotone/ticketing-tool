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
@Table(name = "m_TicketSubStatus")
public class mTicketSubStatus {
    @Id
    @Column(name = "subId", nullable = false,length = 9,unique = true)
    private Long subId;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status")
    private mGeneralMaster status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sub_status")
    private mGeneralMaster sub_status;

    @Column(name = "isactive", columnDefinition = "TINYINT",nullable = false)
    private Boolean isactive;

    @Column(name = "insdate", nullable = true)
    @JsonFormat(pattern = "dd-MM-yyyy 'T' HH:mm:ss")
    private LocalDateTime insertdtm;

    @PrePersist
    protected void onCreate() {
        insertdtm = LocalDateTime.now();
    }




}
