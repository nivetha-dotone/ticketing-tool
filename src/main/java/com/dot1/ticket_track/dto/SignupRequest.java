package com.dot1.ticket_track.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {



    private String userID;

    private String userPWD;

    private int empId;

    private Boolean isActLog;

}
