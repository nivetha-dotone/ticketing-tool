package com.dot1.ticket_track.dto;

import com.dot1.ticket_track.entity.mClientCMEMaster;
import com.dot1.ticket_track.entity.mEmployeeMaster;
import lombok.Data;

@Data
public class JwtAuthenticationResponcse {
    private String token;

    private String refreshToken;

    private String userID;

    private String role;

    private mEmployeeMaster employeeMaster;

    private mClientCMEMaster clientCMEMaster;

}
