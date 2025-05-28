package com.dot1.ticket_track.entity;





import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attachments")
public class Attachment {

    @Id
    @Column(name = "id", nullable = false,length = 9,unique = true)
    private Long id;

    private String fileName;
    private String fileType;

    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticketDt")
    private mTicketSdeatils ticketDt;




}
