package com.dot1.ticket_track.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_newTypes")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "typeid", nullable = false, length = 4)
    private Integer typeid;

    @Column(name = "gmType",unique = true, nullable = false, length = 40)
    private String gmType;

    @Column(name = "TypeDesction",unique = true, nullable = false, length = 40)
    private String gmtypedropdoen;


}


