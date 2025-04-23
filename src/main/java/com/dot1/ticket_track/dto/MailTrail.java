package com.dot1.ticket_track.dto;

import lombok.Data;


@Data
public class MailTrail {
    private String subject;
    private String body;
    private String from;
    private String to;
    private Long ticketidM;
    private String date;



}
