package com.dot1.ticket_track.controller.mailTrail;

import lombok.Data;

@Data
public class MailTrail {
    private String subject;
    private String body;
    private String from;
    private String to;
    private String cc;
    private String bcc;
    private String date;



}
