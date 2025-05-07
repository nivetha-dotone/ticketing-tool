package com.dot1.ticket_track.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Sla_Masters")
public class Sla_Masters {

    @Id
    @Column(name = "slaId", nullable = false, length = 9, unique = true)
    private Long slaId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tickettype")
    private  mGeneralMaster tickettype;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ticketlevel")
    private  mGeneralMaster ticketlevel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "priority")
    private mGeneralMaster priority;

 // to store a timeing only for further calculation

    @Column(name = "sla_time")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime slaTime;



}
