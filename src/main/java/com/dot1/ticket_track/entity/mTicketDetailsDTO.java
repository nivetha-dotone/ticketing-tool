package com.dot1.ticket_track.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
public class mTicketDetailsDTO {
    @NonNull
    private Integer ticketid;
    @NonNull
    private String ticketcode;
    @NonNull
    private Integer tickettype;
    @NonNull
    private Integer ticketlevel;
    @NonNull
    private Integer priority;
    @NonNull
    private String companyname;
    @NonNull
    private Integer clientid;
    @NonNull
    private String ticketnote;
    @NonNull
    private String ticketdesc;
    @NonNull
    private Integer modulesid;
    @NonNull
    private Integer cmexpertId;
    @NonNull
    private Integer employeeId;
    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reportedon;
    @NonNull
    @JsonFormat(pattern = "dd-MM-yyyy 'T' HH:mm:ss")
    private LocalDateTime createdon;
}
