package com.app.vdsp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "images")
public class Image {

    @Id
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @NotBlank(message = "Image path cannot be blank")
    @Column(nullable = false)
    private String path;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "album_id")
    private Album album;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}