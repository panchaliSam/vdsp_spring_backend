package com.app.vdsp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation_documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Reservation reservation;

    private String name;
    private String mimeType;

    @Lob
    private byte[] data;

    private LocalDateTime createdAt;
}
