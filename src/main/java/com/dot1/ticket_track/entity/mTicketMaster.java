//package com.dot1.ticket_track.entity;

//import com.dot1.ticket_track.services.TicketMasterService;
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.annotation.JsonIdentityInfo;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.ObjectIdGenerators;
//import jakarta.persistence.*;
//import lombok.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "mTicketMaster")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ticketid")
//    public class mTicketMaster {
//    @Id
//    @Column(name = "ticketid", nullable = false, unique = true, length = 50)
//    private String ticketid;


////    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
////    @JoinColumn(name = "tickettype", referencedColumnName = "gmid", nullable = false)
////    private mGeneralMaster tickettype;

////    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
////    @JoinColumn(name = "ticketlevel", referencedColumnName = "gmid", nullable = false)
////    private mGeneralMaster ticketlevel;


////    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
////    @JoinColumn(name = "priority", referencedColumnName = "gmid", nullable = false)
////    private mGeneralMaster priority;



//    @Column(name = "companyname", nullable = false, length = 200,unique = true)
//    private String companyname;


////    @JoinColumn(name = "clientid", referencedColumnName = "clientid",nullable = false)
////    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
////    private mClientMaster clientid;


//    @Column(name = "ticketnote", nullable = true, length = 400)
//    private String ticketnote;


//    @Column(name = "ticketdesc", nullable = true, length = -1) // For nvarchar(max)
//    private String ticketdesc;


////    @JoinColumn(name = "modulesid", referencedColumnName = "modId",nullable = false)
////    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
////    private mModulesMaster modulesid;


////    @JoinColumn(name = "cmexpertId", referencedColumnName = "cmeId", nullable = false)
////    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
////    private mClientCMEMaster cmexpertId;


////    @JoinColumn(name = "employeeId", referencedColumnName = "empId", nullable = false)
////    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
////    private mEmployeeMaster employeeId;


//    @Column(name = "ticketcd", nullable = true, length = 200,unique = true)
//    private String ticketcd;


////    @OneToMany(mappedBy = "ticketID", cascade = CascadeType.ALL, orphanRemoval = true)
////    private List<Attachment> attachments = new ArrayList<>();


//    @Column(name = "reportedon", nullable = true)
//    @JsonFormat(pattern = "dd-MM-yyyy 'T' HH:mm:ss")
//    private LocalDateTime reportedon;


//    @Column(name = "createdon", nullable = true)
//    @JsonFormat(pattern = "dd-MM-yyyy 'T' HH:mm:ss")
//    private LocalDateTime createdon;


//    @Transient
//    @JsonIgnore
//    private TicketMasterService ticketMasterService;


//    @PrePersist
//    public void prePersist() {
//        if (createdon == null) {
//            createdon = LocalDateTime.now();
//            reportedon = LocalDateTime.now();
//        }
//        if (ticketid == null) {
//            ticketid = ticketMasterService.generateUniqueTicketId();
//        }
//    }

  // Helper method to add an attachment
////    public void addAttachment(Attachment attachment) {
////        attachments.add(attachment);
////        attachment.setTicketID(this);
////    }
//}
