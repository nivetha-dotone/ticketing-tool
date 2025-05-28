package com.dot1.ticket_track.entity;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mTicketSdeatils")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ticketid")
public class mTicketSdeatils {

    @Id
    @Column(name = "ticketid", nullable = false,length = 9,unique = true)
    private Long ticketid;

    @Column(name = "ticketcode", nullable = false, length = 2000, unique = true)
    private String ticketcode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tickettype")
    private  mGeneralMaster tickettype;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ticketlevel")
    private  mGeneralMaster ticketlevel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "priority")
    private mGeneralMaster priority;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status")
    private mGeneralMaster status;

    @Column(name = "companyname", nullable = false, length = 200)
    private String companyname;

    @JoinColumn(name = "clientid", referencedColumnName = "clientid")
    @ManyToOne(fetch = FetchType.EAGER)
    private mClientMaster clientid;

    @Column(name = "ticketnote", nullable = false, length = 400)
    private String ticketnote;

    @Column(name = "ticketdesc", nullable = false, length = 5000) // For nvarchar(max)
    private String ticketdesc;

    @JoinColumn(name = "modulesid", referencedColumnName = "modId")
    @ManyToOne(fetch = FetchType.EAGER)
    private mModulesMaster modulesid;

    @JoinColumn(name = "cmexpertId", referencedColumnName = "cmeId")
    @ManyToOne(fetch = FetchType.EAGER)
    private mClientCMEMaster cmexpertId;

    @JoinColumn(name = "employeeId", referencedColumnName = "empId")
    @ManyToOne(fetch = FetchType.EAGER)
    private mEmployeeMaster employeeId;

    @OneToMany(mappedBy = "ticketDt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>();

    @Column(name = "reportedon", nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reportedon;

    @Column(name = "createdon", nullable = true)
    @JsonFormat(pattern = "dd-MM-yyyy 'T' HH:mm:ss")
    private LocalDateTime createdon;

    @PrePersist
    public void prePersist() {
        if (createdon == null) {
            createdon = LocalDateTime.now();
        }
    }




}

//select NEWID() from [DOT1TICKETSYSTEM].[dbo].[m_ticket_master];


