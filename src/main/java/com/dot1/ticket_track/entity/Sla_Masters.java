package com.dot1.ticket_track.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.ode.sampling.DummyStepHandler;
import java.time.Duration;


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
    @JoinColumn(name = "tickettypeSal")
    private  mGeneralMaster tickettypeSal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ticketlevelSal")
    private  mGeneralMaster ticketlevelSal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prioritySal")
    private mGeneralMaster priority;

    @Column(name = "sla_time")
    @JsonFormat(pattern = "HH:mm")
    private Duration slaTime;



}