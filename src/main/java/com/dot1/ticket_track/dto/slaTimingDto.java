package com.dot1.ticket_track.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class slaTimingDto {

    private Duration slaTime;

    private Duration Workedtime;

    private Duration RemainingSla;






}
