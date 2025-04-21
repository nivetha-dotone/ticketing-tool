package com.dot1.ticket_track.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class uatTranscationDto {

    private Long uaId;

    private String commentDto;

    private String employeeDto;

    private String empNameDto;

    private String cmeNameDto;

    private Integer cmeexpertDto;

    private Integer statusId;
    private String statusName;

    private tiketSubDto sub_statusDto;

    private Long ticket_numberDto;

    @JsonFormat(pattern = "dd-MM-yyyy 'T' HH:mm:ss")
    private LocalDateTime updateDate;

}
